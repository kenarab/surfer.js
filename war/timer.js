var now = (function() {

  // Returns the number of milliseconds elapsed since either the browser navigationStart event or 
  // the UNIX epoch, depending on availability.
  // Where the browser supports 'performance' we use that as it is more accurate (microsoeconds
  // will be returned in the fractional part) and more reliable as it does not rely on the system time. 
  // Where 'performance' is not available, we will fall back to Date().getTime().

  var performance = window.performance || {};
    
  performance.now = (function() {
    return performance.now    ||
    performance.webkitNow     ||
    performance.msNow         ||
    performance.oNow          ||
    performance.mozNow        ||
    function() { return new Date().getTime(); };
  })();
          
  return performance.now();         
});