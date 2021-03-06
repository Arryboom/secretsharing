/*

Copyright 2014 The MITRE Corporation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

This project contains content developed by The MITRE Corporation. If this 
code is used in a deployment or embedded within another project, it is 
requested that you send an email to opensource@mitre.org in order to let 
us know where this software is being used.

 */

package org.mitre.secretsharing.server;

import java.io.IOException;
import java.io.Writer;
import java.security.SecureRandom;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.secretsharing.Part;
import org.mitre.secretsharing.Secrets;

import com.fasterxml.jackson.core.Base64Variants;

public class FormSplitServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Random rnd = new SecureRandom();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Writer w = new HtmlXSSWriter(resp.getWriter());
		
		try {
			String secret = req.getParameter("secret");
			if(secret == null)
				throw new RuntimeException("No secret parameter");
			int totalParts;
			try {
				totalParts = Integer.parseInt(req.getParameter("total_parts"));
				if(totalParts < 1)
					throw new RuntimeException();
			} catch(Exception e) {
				throw new RuntimeException("Total parts not an integer at least 1.");
			}
			int requiredParts;
			try {
				requiredParts = Integer.parseInt(req.getParameter("required_parts"));
				if(requiredParts < 1 || requiredParts > totalParts)
					throw new RuntimeException();
			} catch(Exception e) {
				throw new RuntimeException("Required parts not an integer at least 1 and not more than total parts.");
			}
			boolean base64 = false;
			if(req.getParameter("base64") != null)
				base64 = Boolean.parseBoolean(req.getParameter("base64"));

			byte[] secretBytes;

			if(base64) {
				try {
					secretBytes = Base64Variants.MIME.decode(secret);
				} catch(Exception e) {
					throw new RuntimeException("Improper encoding of base64 secret");
				}
			} else
				secretBytes = secret.getBytes("UTF-8");

			Part[] parts = Secrets.splitPerByte(secretBytes, totalParts, requiredParts, rnd);

			for(Part part : parts) {
				w.write(part + "\n");
			}
		} catch(Throwable t) {
			if(t.getMessage() != null)
				w.write(t.getMessage());
			else
				w.write("error");
		}
	}
}
