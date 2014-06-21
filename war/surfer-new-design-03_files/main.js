$(document).ready(function() {

    $('#slider input').slider();
    
    
    var formulaInput = $('#controls-row input');
    var buttonsRow = $("#btns-row");
    //if the cursor goes activates the formula
    formulaInput.focus(function() {
        buttonsRow.animate({height:"50px"});
    });

    //and if it loses focus
    formulaInput.blur(function() {
        buttonsRow.animate({height:0});
    });
    
});