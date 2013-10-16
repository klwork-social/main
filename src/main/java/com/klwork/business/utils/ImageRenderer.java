/**
 * Renderer.java
 * Copyright (c) 2005-2009 Radek Burget
 *
 * CSSBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * CSSBox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with CSSBox. If not, see <http://www.gnu.org/licenses/>.
 *
 * Created on 5.2.2009, 12:00:02 by burgetr
 */
package com.klwork.business.utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DefaultDocumentSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.layout.BackgroundImage;
import org.fit.cssbox.layout.BlockBox;
import org.fit.cssbox.layout.Box;
import org.fit.cssbox.layout.BrowserCanvas;
import org.fit.cssbox.layout.BrowserConfig;
import org.fit.cssbox.layout.ElementBox;
import org.fit.cssbox.layout.LengthSet;
import org.fit.cssbox.layout.ReplacedBox;
import org.fit.cssbox.layout.ReplacedContent;
import org.fit.cssbox.layout.ReplacedImage;
import org.fit.cssbox.layout.TextBox;
import org.fit.cssbox.layout.Viewport;
import org.fit.cssbox.layout.VisualContext;
import org.fit.cssbox.misc.Base64Coder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.klwork.common.SystemConstants;
import com.klwork.common.utils.FileUtil;
import com.klwork.common.utils.StringTool;

import cz.vutbr.web.css.CSSProperty;
import cz.vutbr.web.css.TermColor;

/**
 * This class provides a rendering interface for obtaining the document image
 * form an URL.
 * 
 * @author burgetr
 */
public class ImageRenderer
{
    public static final short TYPE_PNG = 0;
    public static final short TYPE_SVG = 1;
    
    protected static final short TURN_ALL = 0; //drawing stages
    protected static final short TURN_NONFLOAT = 1;
    protected static final short TURN_FLOAT = 2;
    protected static final short MODE_BOTH = 0; //drawing modes
    protected static final short MODE_FG = 1;
    protected static final short MODE_BG = 2;
    
    private boolean loadImages = true;
    private boolean loadBackgroundImages = true;

    public void setLoadImages(boolean content, boolean background)
    {
        loadImages = content;
        loadBackgroundImages = background;
    }
    
    /**
     * Renders the URL and prints the result to the specified output stream in the specified
     * format.
     * @param urlstring the source URL
     * @param out output stream
     * @param type output type, one of the TYPE_XXX constants
     * @return true in case of success, false otherwise
     * @throws SAXException 
     */
    public boolean renderURL(String urlstring, OutputStream out, short type) throws IOException, SAXException
    {
        if (!urlstring.startsWith("http:") &&
            !urlstring.startsWith("ftp:") &&
            !urlstring.startsWith("file:"))
                urlstring = "http://" + urlstring;
        
        //Open the network connection 
        DocumentSource docSource = new DefaultDocumentSource(urlstring);
        
        //Parse the input document
        DOMSource parser = new DefaultDOMSource(docSource);
        Document doc = parser.parse();
        
        //Create the CSS analyzer
        DOMAnalyzer da = new DOMAnalyzer(doc, docSource.getURL());
        da.attributesToStyles(); //convert the HTML presentation attributes to inline styles
        da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the standard style sheet
        da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the additional style sheet
        da.getStyleSheets(); //load the author style sheets
        
        if (type == TYPE_PNG)
        {
            BrowserCanvas contentCanvas = new BrowserCanvas(da.getRoot(), da, docSource.getURL());
            contentCanvas.getConfig().setLoadImages(loadImages);
            contentCanvas.getConfig().setLoadBackgroundImages(loadBackgroundImages);
            contentCanvas.createLayout(new java.awt.Dimension(400,700));
            ImageIO.write(contentCanvas.getImage(), "png", out);
        }
        else if (type == TYPE_SVG)
        {
            BrowserCanvas contentCanvas = new BrowserCanvas(da.getRoot(), da, docSource.getURL());
            contentCanvas.getConfig().setLoadImages(loadImages);
            contentCanvas.getConfig().setLoadBackgroundImages(loadBackgroundImages);
            setDefaultFonts(contentCanvas.getConfig());
            contentCanvas.createLayout(new java.awt.Dimension(1200, 600));
            PrintWriter w = new PrintWriter(new OutputStreamWriter(out, "utf-8"));
            writeSVG(contentCanvas.getViewport(), w);
            w.close();
        }
        
        docSource.close();

        return true;
    }
    
    /**
     * Sets some common fonts as the defaults for generic font families.
     */
    protected void setDefaultFonts(BrowserConfig config)
    {
        config.setDefaultFont(Font.SERIF, "Times New Roman");
        config.setDefaultFont(Font.SANS_SERIF, "Arial");
        config.setDefaultFont(Font.MONOSPACED, "Courier New");
    }
    
    protected void writeSVG(Viewport vp, PrintWriter out) throws IOException
    {
        int w = vp.getContentWidth();
        int h = vp.getContentHeight();
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>");
        out.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">");
        out.println("<!-- Rendered by CSSBox http://cssbox.sourceforge.net -->");
        out.println("<svg xmlns=\"http://www.w3.org/2000/svg\"");
        out.println("     xmlns:xlink=\"http://www.w3.org/1999/xlink\" xml:space=\"preserve\"");
        out.println("         width=\"" + w + "\" height=\"" + h + "px\"");
        out.println("         viewBox=\"0 0 " + w + " " + h + "\"");
        out.println("         zoomAndPan=\"disable\" >");
        
        writeBoxSVG(vp, out);
        
        out.println("</svg>");    
    }
    
    protected void writeBoxSVG(Box box, PrintWriter out) throws IOException
    {
        if (box.isVisible())
        {
            out.println("<!-- ********* OUTPUT TURN 1 - Background ********* -->");
            writeBoxSVG(box, out, TURN_NONFLOAT, MODE_BG);
            out.println("<!-- ********* OUTPUT TURN 2 - Floating boxes ********* -->");
            writeBoxSVG(box, out, TURN_FLOAT, MODE_BOTH);
            out.println("<!-- ********* OUTPUT TURN 3 - Foreground ********* -->");
            writeBoxSVG(box, out, TURN_NONFLOAT, MODE_FG);
        }
    }
    
    protected void writeBoxSVG(Box box, PrintWriter out, int turn, int mode) throws IOException
    {
        if (box instanceof TextBox)
            writeTextBoxSVG((TextBox) box, out, turn, mode);
        else
            writeElementBoxSVG((ElementBox) box, out, turn, mode);
    }

    protected void writeTextBoxSVG(TextBox t, PrintWriter out, int turn, int mode) throws IOException
    {
        if (t.isDisplayed() && t.isVisible() &&
            (turn == TURN_ALL || turn == TURN_NONFLOAT) &&
            (mode == MODE_BOTH || mode == MODE_FG))
        {
            Rectangle b = t.getAbsoluteBounds();
            VisualContext ctx = t.getVisualContext();
            
            String style = "font-size:" + ctx.getFont().getSize() + "px;" + 
                           "font-weight:" + (ctx.getFont().isBold()?"bold":"normal") + ";" + 
                           "font-variant:" + (ctx.getFont().isItalic()?"italic":"normal") + ";" +
                           "font-family:" + ctx.getFont().getFamily() + ";" +
                           "fill:" + colorString(ctx.getColor()) + ";" +
                           "stroke:none";
                           
            out.println("<text x=\"" + b.x + "\" y=\"" + (b.y + t.getBaselineOffset()) + "\" width=\"" + b.width + "\" height=\"" + b.height + "\" style=\"" + style + "\">" + htmlEntities(t.getText()) + "</text>");
        }
    }
    
    protected void writeElementBoxSVG(ElementBox eb, PrintWriter out, int turn, int mode) throws IOException
    {
        boolean floating = !(eb instanceof BlockBox) || (((BlockBox) eb).getFloating() != BlockBox.FLOAT_NONE);
        boolean draw = (turn == TURN_ALL || (floating && turn == TURN_FLOAT) || (!floating && turn == TURN_NONFLOAT));
        boolean drawbg = draw && (mode == MODE_BOTH || mode == MODE_BG);
        boolean drawfg = draw && (mode == MODE_BOTH || mode == MODE_FG);
        
        Color bg = eb.getBgcolor();
        Rectangle cb = eb.getAbsoluteContentBounds();
        
        if (drawbg)
        {
            Rectangle bb = eb.getAbsoluteBorderBounds();
            
            //background color
            if (bg != null)
            {
                String style = "stroke:none;fill-opacity:1;fill:" + colorString(bg);
                out.println("<rect x=\"" + bb.x + "\" y=\"" + bb.y + "\" width=\"" + bb.width + "\" height=\"" + bb.height + "\" style=\"" + style + "\" />");
            }
            
            //background image
            if (eb.getBackgroundImages() != null && eb.getBackgroundImages().size() > 0)
            {
                for (BackgroundImage bimg : eb.getBackgroundImages())
                {
                    BufferedImage img = bimg.getBufferedImage();
                    if (img != null)
                    {
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(img, "png", os);
                        char[] data = Base64Coder.encode(os.toByteArray());
                        String imgdata = "data:image/png;base64," + new String(data);
                        int ix = bb.x + eb.getBorder().left;
                        int iy = bb.y + eb.getBorder().top;
                        int iw = bb.width - eb.getBorder().right - eb.getBorder().left;
                        int ih = bb.height - eb.getBorder().bottom - eb.getBorder().top;
                        out.println("<image x=\"" + ix + "\" y=\"" + iy + "\" width=\"" + iw + "\" height=\"" + ih + "\" xlink:href=\"" + imgdata + "\" />");
                    }
                }
                
            }
        
            //border
            LengthSet borders = eb.getBorder();
            if (borders.top > 0)
                writeBorderSVG(eb, bb.x, bb.y, bb.x + bb.width, bb.y, "top", borders.top, 0, borders.top/2, out);
            if (borders.right > 0)
                writeBorderSVG(eb, bb.x + bb.width, bb.y, bb.x + bb.width, bb.y + bb.height, "right", borders.right, -borders.right/2, 0, out);
            if (borders.bottom > 0)
                writeBorderSVG(eb, bb.x, bb.y + bb.height, bb.x + bb.width, bb.y + bb.height, "bottom", borders.bottom, 0, -borders.bottom/2, out);
            if (borders.left > 0)
                writeBorderSVG(eb, bb.x, bb.y, bb.x, bb.y + bb.height, "left", borders.left, borders.left/2, 0, out);
        }
        
        //contents
        if (eb.isReplaced()) //replaced boxes
        {
            if (drawfg)
            {
                ReplacedContent cont = ((ReplacedBox) eb).getContentObj();
                if (cont != null && cont instanceof ReplacedImage) //images
                {
                    BufferedImage img = ((ReplacedImage) cont).getBufferedImage();
                    if (img != null)
                    {
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(img, "png", os);
                        char[] data = Base64Coder.encode(os.toByteArray());
                        String imgdata = "data:image/png;base64," + new String(data);
                        out.println("<image x=\"" + cb.x + "\" y=\"" + cb.y + "\" width=\"" + cb.width + "\" height=\"" + cb.height + "\" xlink:href=\"" + imgdata + "\" />");
                    }
                }
            }
        }
        else
        {
            int nestTurn = turn;
            if (floating && turn == TURN_FLOAT)
                nestTurn = TURN_ALL;
            for (int i = 0; i < eb.getSubBoxNumber(); i++) //contained boxes
                writeBoxSVG(eb.getSubBox(i), out, nestTurn, mode);
        }
    }
    
    protected void writeBorderSVG(ElementBox eb, int x1, int y1, int x2, int y2, String side, int width, int right, int down, PrintWriter out) throws IOException
    {
        TermColor tclr = eb.getStyle().getValue(TermColor.class, "border-"+side+"-color");
        CSSProperty.BorderStyle bst = eb.getStyle().getProperty("border-"+side+"-style");
        if (tclr != null && bst != CSSProperty.BorderStyle.HIDDEN)
        {
            Color clr = tclr.getValue();
            if (clr == null) clr = Color.BLACK;

            String stroke = "";
            if (bst == CSSProperty.BorderStyle.SOLID)
            {
                stroke = "stroke-width:" + width;
            }
            else if (bst == CSSProperty.BorderStyle.DOTTED)
            {
                stroke = "stroke-width:" + width + ";stroke-dasharray:" + width + "," + width;
            }
            else if (bst == CSSProperty.BorderStyle.DASHED)
            {
                stroke = "stroke-width:" + width + ";stroke-dasharray:" + (3*width) + "," + width;
            }
            else if (bst == CSSProperty.BorderStyle.DOUBLE)
            {
                //double is not supported yet, we'll use single
                stroke = "stroke-width:" + width;
            }
            else //default or unsupported - draw a solid line
            {
                stroke = "stroke-width:" + width;
            }
            
            String coords = "M " + (x1+right) + "," + (y1+down) + " L " + (x2+right) + "," + (y2+down);
            String style = "fill:none;stroke:" + colorString(clr) + ";" + stroke;
            out.println("<path style=\"" + style + "\" d=\"" + coords + "\" />");  
        }
        
    }
    
    protected String colorString(Color color)
    {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
    
    protected String htmlEntities(String s)
    {
        return s.replaceAll(">", "&gt;").replaceAll("<", "&lt;").replaceAll("&", "&amp;");
    }
    
    //=================================================================================
    
    public static void main(String[] args)
    {
        
        try {
            short type = -1;
            String imageType = "png";
			if (imageType.equalsIgnoreCase("png"))
                type = TYPE_PNG;
            else if (imageType.equalsIgnoreCase("svg"))
                type = TYPE_SVG;
            else
            {
                System.err.println("Error: unknown format");
                System.exit(0);
            }
            
            String outFile = "e:/opt/good.png";
			FileOutputStream os = new FileOutputStream(outFile);
            
            ImageRenderer r = new ImageRenderer();
            //String urlstring = "http://127.0.0.1/ks/out/show-weibo-content";
            
            String pathPrefix = "/com/klwork/weibo";
    		Map root = new HashMap();
    		String value = "从武侠看人才     好的杀手应该分成两种,一种是顶级的,一种是优秀的。什么是顶级的？这种人武功已经达到化境，这是一个什么的境界？也就是说对于他来说，已经没有招式可言,你可以想想张三丰、扫地僧、风清扬的样子。路上的一片树叶、树枝、石头什么的，足以使对手毙命，老板们最喜欢的是这类人才，但是可遇不可求。"+
 "还有的就是优秀的人才，这种人应该不少,他们的武功也很高，且多有一颗可怀才不遇的心，交代的事情一般都能搞定。 "+
"但是出任务前，老板会给他匹配相应的团队，可能要叮嘱他一下哪些危险的地方，建议他用什么招式等等,"+ 
"不然有可能回不来了。这种人还有个致命的缺陷，他一直以为自己是个顶级杀手。更要命的是老板也把他当成了第一种人。";
    		StringBuilder fcontent = StringTool.getSplitString(
    				value, "\n", 16);
    		
			root.put("content", fcontent.toString());
    		StringBuffer b = FileUtil.getContetByFreemarker(ImageRenderer.class,
    				"weiboImageContent.xhtml", pathPrefix, root);
    		System.out.println(b);
    		String fileurl = HtmlTranslateImageTool.currentFilePathByKey("weibo","11345","html");
			FileUtil.writeFile(b.toString(),fileurl);
            String fileCurrent = "file:///" + fileurl.replace("\\", "/");//file:///D:/weibo-temp/weibo/11345.html
			r.renderURL(fileCurrent, os, type);
            
            os.close();
            System.err.println("Done.");
        } catch (Exception e) {
        	e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    
    public static void main2(String[] args){
    	String pathPrefix = "/com/klwork/weibo";
		Map root = new HashMap();
		root.put("wish", "asdfasfd");
		StringBuffer b = FileUtil.getContetByFreemarker(ImageRenderer.class,
				"weiboImageContent.xhtml", pathPrefix, root);
		System.out.println(b);
		/*HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		String htmlstr = b.toString();
		imageGenerator.loadHtml(htmlstr);
		imageGenerator.getBufferedImage();
		 String relativePath = HtmlTranslateImageTool.currentFilePathByKey("weibo","11345",".png");
		imageGenerator.saveAsImage(relativePath);*/
    }
}
