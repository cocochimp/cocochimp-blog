webpackJsonp([9],{"0FLT":function(e,t){},IcnI:function(e,t,n){"use strict";var a=n("7+uW"),o=n("NYxO");a.default.use(o.a);var i={loading:!1,themeObj:0,keywords:"",errorImg:'this.onerror=null;this.src="'+n("jQBE")+'"',baseURL:"http://116.205.191.163:7777"};t.a=new o.a.Store({state:i})},NHnr:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=n("7+uW"),o={render:function(){var e=this.$createElement,t=this._self._c||e;return t("div",{attrs:{id:"app"}},[t("keep-alive",{attrs:{include:["Aboutme","Message","FriendsLink","Reward"]}},[t("router-view")],1)],1)},staticRenderFns:[]};var i=n("VU/8")({name:"App"},o,!1,function(e){n("0FLT")},null,null).exports,r=n("YaEn"),c=n("zL8q"),u=n.n(c),l=(n("q8zI"),n("PijW"),n("IcnI")),s=n("OS1Z"),p=n.n(s);var f=n("cTzj"),h=n.n(f);a.default.config.productionTip=!1,a.default.use(u.a),a.default.use(p.a),a.default.prototype.floatElement=function(e){$(window).scroll(function(e){var t=$(".floatElement"),n="fixed"==t.css("position");$(this).scrollTop()>200&&!n&&t.css({position:"fixed",top:"0px"}),$(this).scrollTop()<200&&n&&t.css({position:"static",top:"0px"})}),console.log("ccc")},a.default.use(h.a,{preLoad:1.3,error:n("fDcx"),loading:n("fDcx"),attempt:5}),new a.default({el:"#app",router:r.a,components:{App:i},template:"<App/>",store:l.a})},PijW:function(e,t){},YaEn:function(e,t,n){"use strict";var a=n("7+uW"),o=n("/ocq");a.default.use(o.a),t.a=new o.a({scrollBehavior:function(e,t,n){return n||{x:0,y:window.innerWidth>=700?676:267}},routes:[{path:"/",component:function(e){return Promise.all([n.e(0),n.e(1)]).then(function(){var t=[n("HXef")];e.apply(null,t)}.bind(this)).catch(n.oe)},meta:{auth:!0},name:"Home"},{path:"/Home",component:function(e){return Promise.all([n.e(0),n.e(1)]).then(function(){var t=[n("HXef")];e.apply(null,t)}.bind(this)).catch(n.oe)},meta:{auth:!0},name:"Home"},{path:"/Share",component:function(e){return Promise.all([n.e(0),n.e(4)]).then(function(){var t=[n("zJHd")];e.apply(null,t)}.bind(this)).catch(n.oe)},meta:{auth:!0},name:"Share"},{path:"/DetailArticle",component:function(e){return Promise.all([n.e(0),n.e(3)]).then(function(){var t=[n("eDx+")];e.apply(null,t)}.bind(this)).catch(n.oe)},meta:{auth:!0},name:"DetailArticle"},{path:"/Reward",component:function(e){return Promise.all([n.e(0),n.e(6)]).then(function(){var t=[n("gejy")];e.apply(null,t)}.bind(this)).catch(n.oe)},meta:{auth:!0},name:"Reward"},{path:"/FriendsLink",component:function(e){return Promise.all([n.e(0),n.e(2)]).then(function(){var t=[n("pUly")];e.apply(null,t)}.bind(this)).catch(n.oe)},meta:{auth:!0},name:"FriendsLink"},{path:"/Login",component:function(e){return Promise.all([n.e(0),n.e(5)]).then(function(){var t=[n("P7ry")];e.apply(null,t)}.bind(this)).catch(n.oe)},meta:{auth:!1},name:"Login"},{path:"/UserInfo",component:function(e){return Promise.all([n.e(0),n.e(7)]).then(function(){var t=[n("psK5")];e.apply(null,t)}.bind(this)).catch(n.oe)},meta:{auth:!0},name:"UserInfo"}]})},fDcx:function(e,t,n){e.exports=n.p+"static/img/懒加载.gif?v=11185b0"},jQBE:function(e,t,n){e.exports=n.p+"static/img/tou.jpg?v=162d766"},q8zI:function(e,t){}},["NHnr"]);