var Inheritance = function(){};

Inheritance.W = function(){};

Inheritance.W.W = "W";
Inheritance.W.$typeDescription={};

Inheritance.X = function(){};

Inheritance.X.X = "X";
Inheritance.X.$typeDescription={};

Inheritance.Y = function(){};

Inheritance.Y.Y = "Y";
Inheritance.Y.$typeDescription={};

Inheritance.Z = function(){};

Inheritance.Z.Z = "Z";
Inheritance.Z.$typeDescription={};

Inheritance.A = function(){};

Inheritance.A.prototype.method1 = function(n) {
    return n;
};
Inheritance.A.prototype.method2 = function(n) {
    return this.method1(n + 1);
};
Inheritance.A.$typeDescription={};

Inheritance.B = function(){Inheritance.A.call(this);};

stjs.extend(Inheritance.B, Inheritance.A);

Inheritance.B.prototype.method1 = function(n) {
    return Inheritance.A.prototype.method1.call(this, n + 1);
};
Inheritance.B.prototype.method2 = function(n) {
    return Inheritance.A.prototype.method2.call(this, n + 1);
};
Inheritance.B.$typeDescription=stjs.copyProps(Inheritance.A.$typeDescription, {});

Inheritance.C = function(){Inheritance.B.call(this);};

stjs.extend(Inheritance.C, Inheritance.B, Inheritance.W, Inheritance.X, Inheritance.Y, Inheritance.Z);

Inheritance.C.$typeDescription=stjs.copyProps(Inheritance.B.$typeDescription, {});

//
Inheritance.main = function(args) {
    var b = new Inheritance.B();
    var c = new Inheritance.C();
    console.log(b.method2(1) + " " + c.method1(1) + " " + Inheritance.C.W + Inheritance.C.X + Inheritance.C.Y + Inheritance.C.Z + " " + c.W);
};
Inheritance.$typeDescription={};

if (!stjs.mainCallDisabled) Inheritance.main();
