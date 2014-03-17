function gwt_mini_surfer(){
  var $intern_27 = '', $intern_11 = ' top: -1000px;', $intern_36 = '" for "gwt:onLoadErrorFn"', $intern_34 = '" for "gwt:onPropertyErrorFn"', $intern_20 = '");', $intern_37 = '#', $intern_50 = '.cache.js', $intern_39 = '/', $intern_45 = '//', $intern_49 = ':', $intern_28 = '::', $intern_12 = '<html><head><\/head><body><\/body><\/html>', $intern_31 = '=', $intern_38 = '?', $intern_33 = 'Bad handler "', $intern_18 = 'Chrome', $intern_17 = 'DOMContentLoaded', $intern_6 = 'DUMMY', $intern_48 = 'FB819CDEB514980D6C47C7CF82726EE6', $intern_44 = 'base', $intern_42 = 'baseUrl', $intern_1 = 'begin', $intern_7 = 'body', $intern_0 = 'bootstrap', $intern_41 = 'clear.cache.gif', $intern_30 = 'content', $intern_52 = 'end', $intern_19 = 'eval("', $intern_2 = 'gwt.codesvr.gwt_mini_surfer=', $intern_3 = 'gwt.codesvr=', $intern_35 = 'gwt:onLoadErrorFn', $intern_32 = 'gwt:onPropertyErrorFn', $intern_29 = 'gwt:property', $intern_4 = 'gwt_mini_surfer', $intern_47 = 'gwt_mini_surfer.devmode.js', $intern_43 = 'gwt_mini_surfer.nocache.js', $intern_26 = 'gwt_mini_surfer::', $intern_23 = 'head', $intern_8 = 'iframe', $intern_40 = 'img', $intern_14 = 'javascript', $intern_9 = 'javascript:""', $intern_51 = 'loadExternalRefs', $intern_24 = 'meta', $intern_22 = 'moduleRequested', $intern_21 = 'moduleStartup', $intern_25 = 'name', $intern_10 = 'position:absolute; width:0; height:0; border:none; left: -1000px;', $intern_13 = 'script', $intern_46 = 'selectingPermutation', $intern_5 = 'startup', $intern_16 = 'undefined', $intern_15 = 'var $wnd = window.parent;';
  var $wnd = window;
  var $doc = document;
  sendStats($intern_0, $intern_1);
  function isHostedMode(){
    var query = $wnd.location.search;
    return query.indexOf($intern_2) != -1 || query.indexOf($intern_3) != -1;
  }

  function sendStats(evtGroupString, typeString){
    if ($wnd.__gwtStatsEvent) {
      $wnd.__gwtStatsEvent({moduleName:$intern_4, sessionId:$wnd.__gwtStatsSessionId, subSystem:$intern_5, evtGroup:evtGroupString, millis:(new Date).getTime(), type:typeString});
    }
  }

  gwt_mini_surfer.__sendStats = sendStats;
  gwt_mini_surfer.__moduleName = $intern_4;
  gwt_mini_surfer.__errFn = null;
  gwt_mini_surfer.__moduleBase = $intern_6;
  gwt_mini_surfer.__softPermutationId = 0;
  gwt_mini_surfer.__computePropValue = null;
  gwt_mini_surfer.__getPropMap = null;
  gwt_mini_surfer.__gwtInstallCode = function(){
  }
  ;
  gwt_mini_surfer.__gwtStartLoadingFragment = function(){
    return null;
  }
  ;
  var __gwt_isKnownPropertyValue = function(){
    return false;
  }
  ;
  var __gwt_getMetaProperty = function(){
    return null;
  }
  ;
  __propertyErrorFunction = null;
  var activeModules = $wnd.__gwt_activeModules = $wnd.__gwt_activeModules || {};
  activeModules[$intern_4] = {moduleName:$intern_4};
  var frameDoc;
  function getInstallLocationDoc(){
    setupInstallLocation();
    return frameDoc;
  }

  function getInstallLocation(){
    setupInstallLocation();
    return frameDoc.getElementsByTagName($intern_7)[0];
  }

  function setupInstallLocation(){
    if (frameDoc) {
      return;
    }
    var scriptFrame = $doc.createElement($intern_8);
    scriptFrame.src = $intern_9;
    scriptFrame.id = $intern_4;
    scriptFrame.style.cssText = $intern_10 + $intern_11;
    scriptFrame.tabIndex = -1;
    $doc.body.appendChild(scriptFrame);
    frameDoc = scriptFrame.contentDocument;
    if (!frameDoc) {
      frameDoc = scriptFrame.contentWindow.document;
    }
    frameDoc.open();
    frameDoc.write($intern_12);
    frameDoc.close();
    var frameDocbody = frameDoc.getElementsByTagName($intern_7)[0];
    var script = frameDoc.createElement($intern_13);
    script.language = $intern_14;
    var temp = $intern_15;
    script.text = temp;
    frameDocbody.appendChild(script);
  }

  function installScript(filename){
    function setupWaitForBodyLoad(callback){
      function isBodyLoaded(){
        if (typeof $doc.readyState == $intern_16) {
          return typeof $doc.body != $intern_16 && $doc.body != null;
        }
        return /loaded|complete/.test($doc.readyState);
      }

      var bodyDone = isBodyLoaded();
      if (bodyDone) {
        callback();
        return;
      }
      function onBodyDone(){
        if (!bodyDone) {
          bodyDone = true;
          callback();
          if ($doc.removeEventListener) {
            $doc.removeEventListener($intern_17, onBodyDone, false);
          }
          if (onBodyDoneTimerId) {
            clearInterval(onBodyDoneTimerId);
          }
        }
      }

      if ($doc.addEventListener) {
        $doc.addEventListener($intern_17, onBodyDone, false);
      }
      var onBodyDoneTimerId = setInterval(function(){
        if (isBodyLoaded()) {
          onBodyDone();
        }
      }
      , 50);
    }

    function installCode(code){
      function removeScript(body, element){
      }

      var docbody = getInstallLocation();
      var doc = getInstallLocationDoc();
      var script;
      if (navigator.userAgent.indexOf($intern_18) > -1 && window.JSON) {
        var scriptFrag = doc.createDocumentFragment();
        scriptFrag.appendChild(doc.createTextNode($intern_19));
        for (var i = 0; i < code.length; i++) {
          var c = window.JSON.stringify(code[i]);
          scriptFrag.appendChild(doc.createTextNode(c.substring(1, c.length - 1)));
        }
        scriptFrag.appendChild(doc.createTextNode($intern_20));
        script = doc.createElement($intern_13);
        script.language = $intern_14;
        script.appendChild(scriptFrag);
        docbody.appendChild(script);
        removeScript(docbody, script);
      }
       else {
        for (var i = 0; i < code.length; i++) {
          script = doc.createElement($intern_13);
          script.language = $intern_14;
          script.text = code[i];
          docbody.appendChild(script);
          removeScript(docbody, script);
        }
      }
    }

    gwt_mini_surfer.onScriptDownloaded = function(code){
      setupWaitForBodyLoad(function(){
        installCode(code);
      }
      );
    }
    ;
    sendStats($intern_21, $intern_22);
    var script = $doc.createElement($intern_13);
    script.src = filename;
    $doc.getElementsByTagName($intern_23)[0].appendChild(script);
  }

  gwt_mini_surfer.__startLoadingFragment = function(fragmentFile){
    return computeUrlForResource(fragmentFile);
  }
  ;
  gwt_mini_surfer.__installRunAsyncCode = function(code){
    var docbody = getInstallLocation();
    var script = getInstallLocationDoc().createElement($intern_13);
    script.language = $intern_14;
    script.text = code;
    docbody.appendChild(script);
  }
  ;
  function processMetas(){
    var metaProps = {};
    var propertyErrorFunc;
    var onLoadErrorFunc;
    var metas = $doc.getElementsByTagName($intern_24);
    for (var i = 0, n = metas.length; i < n; ++i) {
      var meta = metas[i], name = meta.getAttribute($intern_25), content;
      if (name) {
        name = name.replace($intern_26, $intern_27);
        if (name.indexOf($intern_28) >= 0) {
          continue;
        }
        if (name == $intern_29) {
          content = meta.getAttribute($intern_30);
          if (content) {
            var value, eq = content.indexOf($intern_31);
            if (eq >= 0) {
              name = content.substring(0, eq);
              value = content.substring(eq + 1);
            }
             else {
              name = content;
              value = $intern_27;
            }
            metaProps[name] = value;
          }
        }
         else if (name == $intern_32) {
          content = meta.getAttribute($intern_30);
          if (content) {
            try {
              propertyErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_33 + content + $intern_34);
            }
          }
        }
         else if (name == $intern_35) {
          content = meta.getAttribute($intern_30);
          if (content) {
            try {
              onLoadErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_33 + content + $intern_36);
            }
          }
        }
      }
    }
    __gwt_getMetaProperty = function(name){
      var value = metaProps[name];
      return value == null?null:value;
    }
    ;
    __propertyErrorFunction = propertyErrorFunc;
    gwt_mini_surfer.__errFn = onLoadErrorFunc;
  }

  function computeScriptBase(){
    function getDirectoryOfFile(path){
      var hashIndex = path.lastIndexOf($intern_37);
      if (hashIndex == -1) {
        hashIndex = path.length;
      }
      var queryIndex = path.indexOf($intern_38);
      if (queryIndex == -1) {
        queryIndex = path.length;
      }
      var slashIndex = path.lastIndexOf($intern_39, Math.min(queryIndex, hashIndex));
      return slashIndex >= 0?path.substring(0, slashIndex + 1):$intern_27;
    }

    function ensureAbsoluteUrl(url){
      if (url.match(/^\w+:\/\//)) {
      }
       else {
        var img = $doc.createElement($intern_40);
        img.src = url + $intern_41;
        url = getDirectoryOfFile(img.src);
      }
      return url;
    }

    function tryMetaTag(){
      var metaVal = __gwt_getMetaProperty($intern_42);
      if (metaVal != null) {
        return metaVal;
      }
      return $intern_27;
    }

    function tryNocacheJsTag(){
      var scriptTags = $doc.getElementsByTagName($intern_13);
      for (var i = 0; i < scriptTags.length; ++i) {
        if (scriptTags[i].src.indexOf($intern_43) != -1) {
          return getDirectoryOfFile(scriptTags[i].src);
        }
      }
      return $intern_27;
    }

    function tryBaseTag(){
      var baseElements = $doc.getElementsByTagName($intern_44);
      if (baseElements.length > 0) {
        return baseElements[baseElements.length - 1].href;
      }
      return $intern_27;
    }

    function isLocationOk(){
      var loc = $doc.location;
      return loc.href == loc.protocol + $intern_45 + loc.host + loc.pathname + loc.search + loc.hash;
    }

    var tempBase = tryMetaTag();
    if (tempBase == $intern_27) {
      tempBase = tryNocacheJsTag();
    }
    if (tempBase == $intern_27) {
      tempBase = tryBaseTag();
    }
    if (tempBase == $intern_27 && isLocationOk()) {
      tempBase = getDirectoryOfFile($doc.location.href);
    }
    tempBase = ensureAbsoluteUrl(tempBase);
    return tempBase;
  }

  function computeUrlForResource(resource){
    if (resource.match(/^\//)) {
      return resource;
    }
    if (resource.match(/^[a-zA-Z]+:\/\//)) {
      return resource;
    }
    return gwt_mini_surfer.__moduleBase + resource;
  }

  function getCompiledCodeFilename(){
    var answers = [];
    var softPermutationId;
    var values = [];
    var providers = [];
    function computePropValue(propName){
      var value = providers[propName](), allowedValuesMap = values[propName];
      if (value in allowedValuesMap) {
        return value;
      }
      var allowedValuesList = [];
      for (var k in allowedValuesMap) {
        allowedValuesList[allowedValuesMap[k]] = k;
      }
      if (__propertyErrorFunc) {
        __propertyErrorFunc(propName, allowedValuesList, value);
      }
      throw null;
    }

    __gwt_isKnownPropertyValue = function(propName, propValue){
      return propValue in values[propName];
    }
    ;
    gwt_mini_surfer.__getPropMap = function(){
      var result = {};
      for (var key in values) {
        result[key] = computePropValue(key);
      }
      return result;
    }
    ;
    gwt_mini_surfer.__computePropValue = computePropValue;
    $wnd.__gwt_activeModules[$intern_4].bindings = gwt_mini_surfer.__getPropMap;
    sendStats($intern_0, $intern_46);
    if (isHostedMode()) {
      return computeUrlForResource($intern_47);
    }
    var strongName;
    try {
      strongName = $intern_48;
      var idx = strongName.indexOf($intern_49);
      if (idx != -1) {
        softPermutationId = parseInt(strongName.substring(idx + 1), 10);
        strongName = strongName.substring(0, idx);
      }
    }
     catch (e) {
    }
    gwt_mini_surfer.__softPermutationId = softPermutationId;
    return computeUrlForResource(strongName + $intern_50);
  }

  function loadExternalStylesheets(){
    if (!$wnd.__gwt_stylesLoaded) {
      $wnd.__gwt_stylesLoaded = {};
    }
    sendStats($intern_51, $intern_1);
    sendStats($intern_51, $intern_52);
  }

  processMetas();
  gwt_mini_surfer.__moduleBase = computeScriptBase();
  activeModules[$intern_4].moduleBase = gwt_mini_surfer.__moduleBase;
  var filename = getCompiledCodeFilename();
  loadExternalStylesheets();
  sendStats($intern_0, $intern_52);
  installScript(filename);
  return true;
}

gwt_mini_surfer.succeeded = gwt_mini_surfer();
