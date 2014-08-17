/**
 * you need this file if you use typefy and stringify
 */
stjs.converters2 = {
	Date : function(s, type) {
		var a = /^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2}(?:\.\d*)?)$/
				.exec(s);
		if (a) {
			return new Date(+a[1], +a[2] - 1, +a[3], +a[4], +a[5], +a[6]);
		}
		return null;
	},

	Enum : function(s, type){
		return eval(type.arguments[0])[s];
	}
};

stjs.serializers2 = {
		Date : function(d, type) {
			function pad(n){
				return n < 10 ? "0" + n : "" + n;
			}
			if (d) {
				return "" + d.getFullYear() + "-" + pad(d.getMonth()+1) + "-" + pad(d.getDate()) + " " + pad(d.getHours()) + ":" + pad(d.getMinutes()) + ":" + pad(d.getSeconds());
			}
			return null;
		},

		Enum : function(e, type){
			return e != null ? e.toString() : null;
		}
	};

stjs.isArray=function( obj ) {
    return stjs.toString.call(obj) === "[object Array]";
};

/**
 * cls can by the type of the return. 
 * If it's an array it can be either the type of an element or the type definition of the field.
 * TODO - for other collections and classes is not done yet
 */
stjs.typefy=function(obj, cls){
	if (stjs.isArray(obj)){
		var result = [];
		for(var idx in obj){
			result.push(stjs.typefy(obj[idx], elementType(cls)));
		}
		return result;
	}
	 var constructors = {};
	 function constr(name, param){
		  var c = constructors[name];
		  if (!c)
			  constructors[name] = c = eval(name);
		  return new c(param);
	  }

	 function elementType(type){
		 if (typeof type == "function")
			 return type;
		 if (type.arguments) {
			 return eval(type.arguments[0]);
		 }
		 if (typeof type == "string")
			 return eval(type);
		 return Object;
	  }

	 
	function convert(type, json){
		  if (!type)
			  return json;
		  var cv = stjs.converters2[type.name || type];
		  if (cv)
			  return cv(json, type);
		  //hopefully the type has a string constructor
		 return constr(type, json);
	  }

	 function builder(type){
		  if (!type)
			  return {};
			if (typeof type == "function")
				return new type();
			if (type.name) {
				if (type.name == "Map")
					return {};
				if (type.name == "Array")
					return [];
				return constr(type.name);
			}
			return constr(type);
	  }
	 
	  if (obj == null)
		  return null;
	  
	  var ret = new cls();
	  for(var key in obj){
		  var prop = obj[key];
		  if (prop == null)
			  continue;
		  var td = cls.$typeDescription[key];
		  if (!td) {
			  ret[key] = prop;
			  continue;
		  }
		  if (typeof prop == "string")
			  ret[key] = convert(td, prop);
		  else if (typeof prop == "object")
			  ret[key] = stjs.typefy(prop, td);
	  }
	  return ret;
};

stjs.stringify=function(obj, cls){
	 if (obj == null)
		  return null;
	  
	 var ret = {};
	  for(var key in obj){
		  var td = cls.$typeDescription[key];
		  var prop = obj[key];
		  var ser = td != null ? stjs.serializers2[td.name || td] : null;
		  if (!td || !ser) {
			  ret[key] = prop;
			  continue;
		  }
		  if (typeof prop != "string") 
			  if (ser)
				  ret[key] = ser(prop, td);
			  else
				  ret[key] = stjs.typefy(prop, td);
	  }
	  return ret;
};