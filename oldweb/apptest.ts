export{}

var describe: any;
var it: any;
var expect: any;

describe("Tests of basic math functions", function() {
    it("Adding 1 should work", function() {
        var foo = 0;
        foo += 1;
        expect(foo).toEqual(1);
    });

    it("Subtracting 1 should work", function () {
        var foo = 0;
        foo -= 1;
        expect(foo).toEqual(-1);
    });

    it("UI Test: Add Button Hides Listing", function(){
        // click the button for showing the add button
        $('#showFormButton').click();
        // expect that the add form is not hidden
        expect($("#addElement").attr("style").indexOf("display: none;")).toEqual(-1);
        // expect tha tthe element listing is hidden
        expect($("#showElements").attr("style").indexOf("display: none;")).toEqual(0);
        // reset the UI, so we don't mess up the next test
        $('#addCancel').click();        
    });
});


/**Tests correct object behavior looking for valid responses when new values are submitted and a message is liked or disliked.
 */
describe("Test for correct object behavior", function() {
    
    it("Like button should add a like", function() {
       
        $("#likebtn value=5").click();
        expect(Response).toEqual(200);
    });

    it("Dislike button should add a dislike", function() {
       
        $("#dislikebtn value=5").click();
        expect(Response).toEqual(200);
    });
        
        
    it("Clear form should clear", function() {
        //$("#addCancel").click();
        newEntryForm.clearForm();
        expect($("#newTitle").val()).toEqual("");
        expect($("#newMessage").val()).toEqual("");
    });
    it("Add message should add row", function() {
        $("#newTitle").val().setValue("test");
        $("#newMessage").val().setValue("test");
        $("#addButton").click();
        expect(Response).toEqual(200);
    });
    
    
});

