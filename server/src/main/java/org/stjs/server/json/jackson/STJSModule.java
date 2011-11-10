/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.server.json.jackson;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.module.SimpleModule;
import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.Map;

public class STJSModule {
	public static Module getModule() {
		SimpleModule module = new SimpleModule("MyModule", new Version(1, 0, 0, null));
		module.addSerializer(new JSArraySerializer());
		module.addSerializer(new JSMapSerializer());
		module.addSerializer(new JSDateSerializer());
		module.addDeserializer(Array.class, new JSArrayDeserializer());
		module.addDeserializer(Map.class, new JSMapDeserializer());
		module.addDeserializer(Date.class, new JSDateDeserializer());
		return module;
	}
}
