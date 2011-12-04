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
package org.stjs.testing.jquery;

import org.stjs.javascript.Date;
import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.DataType;
import org.stjs.javascript.functions.Callback1;

@DataType
public class MockjaxOptions {
	/**
	 * A string or regular expression specifying the url of the request that the data should be mocked for. If the url
	 * is a string and contains an asterisk ( * ), it is treated as a wildcard, by translating to a regular expression,
	 * replacing the asterisk with .+.
	 */
	public String url;

	/**
	 * In addition to the URL, match parameters.
	 */
	public Object data;

	/**
	 * Specify what HTTP method to match, usually GET or POST. Case-insensitive, so get and post also work.
	 */
	public String type;

	/**
	 * An object literal whos keys will be simulated as additional headers returned from the server for the request.
	 */
	public Map<String, ? extends Object> headers;

	/**
	 * An integer that specifies a valid server response code. This simulates a server response code.
	 */
	public int status;

	/**
	 * An integer that specifies a simulated network and server latency (in milliseconds).
	 */
	public long responseTime;

	/**
	 * A boolean value that determines whether or not the mock will force a timeout on the request.
	 */
	public boolean isTimeout;

	/**
	 * A string which specifies the content type for the response.
	 */
	public String contentType;

	/**
	 * function(settings) {}, A function that allows for the dynamic setting of responseText/responseXML upon each
	 * request.
	 */
	public Callback1<Map<String, Object>> response;

	/**
	 * A string specifying the mocked text, or a mocked object literal, for the request.
	 */
	public Object responseText;

	/**
	 * A string specifying the mocked XML for the request.
	 */
	public String responseXML;

	/**
	 * A string specifying a path to a file, from which the contents will be returned for the request.
	 */
	public String proxy;

	/**
	 * A date string specifying the mocked last-modified time for the request. This is used by $.ajax to determine if
	 * the requested data is new since the last request.
	 */
	public Date lastModified;

	/**
	 * A string specifying a unique identifier referencing a specific version of the requested data. This is used by
	 * $.ajax to determine if the requested data is new since the last request. (see HTTP_ETag)
	 */
	public String etag;

	public boolean async;
}
