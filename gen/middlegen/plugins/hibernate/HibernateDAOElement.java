/*
 * 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of BEKK Consulting nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package middlegen.plugins.hibernate;


/**
 * This class will generate a simple hibernate DAO class.
 *
 * @author Ben Litchfield
 * @created 26 April 2004
 */
public class HibernateDAOElement extends HibernateBaseElement{

	public HibernateDAOElement(){
		_suffix="DAO";
	}

   /** The java src code snippet to access the hibernate session. */
   private String _getHibernateSession = "net.sf.hibernate.jta.DBHelper.getSession();";

   /** The java src code snippet to close the hibernate session */
   private String _closeHibernateSession = "";

 
   /**
    * Set the new hibernate session
    *
    * @param session The java code snippet for the hibernate session.
    */
   public void setGetHibernateSession(String session) {
      _getHibernateSession = session;
   }


   /**
    * close the new hibernate session
    *
    * @param close The new CloseHibernateSession value
    */
   public void setCloseHibernateSession(String close) {
      _closeHibernateSession = close;
   }

   /**
    * get the hibernate session.
    *
    * @return The hibernate session code.
    */
   public String getGetHibernateSession() {
      return _getHibernateSession;
   }


   /**
    * close the hibernate session.
    *
    * @return The hibernate session code.
    */
   public String getCloseHibernateSession() {
      return _closeHibernateSession;
   }
}
