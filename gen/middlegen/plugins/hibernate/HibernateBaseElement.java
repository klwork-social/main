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
 * This class will generate base hibernate element.
 * 
 */
public class HibernateBaseElement {

	/** The outer plugin used for this feature. */
	HibernatePlugin _plugin;

	/** The suffix to the generated class. */
	String _suffix = "";
	
	/** The preffix to the generated class. */
	String _prefix = "";
	
	String _version = "";
	
	String _dir = "";
	
	String _interFaceDir = "";
	
	String _interFaceSuffix = "";

	public String getInterFaceSuffix() {
		return _interFaceSuffix;
	}

	public void setInterFaceSuffix(String interFaceSuffix) {
		_interFaceSuffix = interFaceSuffix;
	}

	public String getInterFaceDir() {
		return _interFaceDir;
	}
	
	public String getClassInterFaceDir() {
		if(_interFaceDir != null)
			return _interFaceDir.trim().replace("/", ".");
		return _interFaceDir;
	}

	public void setInterFaceDir(String interFaceDir) {
		_interFaceDir = interFaceDir;
	}

	public String getVersion() {
		return _version;
	}

	public void setVersion(String version) {
		this._version = version;
	}

	/**
	 * Set the new dao suffix
	 * 
	 * @param suffix
	 *            The new dao suffix.
	 */
	public void setSuffix(String suffix) {
		_suffix = suffix;
	}

	/**
	 * get the dao suffix.
	 * 
	 * @return The new dao suffix.
	 */
	public String getSuffix() {
		return _suffix;
	}

	/**
	 * Gets the Plugin attribute of the HibernateDAO object
	 * 
	 * @return The Plugin value
	 */
	protected HibernatePlugin getPlugin() {
		return _plugin;
	}

	/**
	 * Sets the Plugin attribute of the HibernateDAO object
	 * 
	 * @param plugin
	 *            The new Plugin value
	 */
	void setPlugin(HibernatePlugin plugin) {
		_plugin = plugin;
	}

	public String getPrefix() {
		return _prefix;
	}

	public void setPrefix(String prefix) {
		_prefix = prefix;
	}

	public String getDir() {
		return _dir;
	}

	public void setDir(String dir) {
		_dir = dir;
	}
	
	/**
	 * @return Returns the _dir.
	 */
	public String getClassDir() {
		if(_dir != null)
			return _dir.trim().replace("/", ".");
		return _dir;
	}

}
