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

package org.mitre.secretsharing.cli;

import org.mitre.secretsharing.cli.cmd.Command;
import org.mitre.secretsharing.cli.cmd.Commands;

public class SecretsCLI {
	public static void main(String[] args) throws Exception {
		Command root = Commands.rootCommand();
		root.perform(root.parse(args), System.in, System.out, System.err);
		System.out.flush();
		System.err.flush();
	}
}