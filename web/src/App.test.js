import { render, screen } from '@testing-library/react';
import App from './App';

test('renders learn react link', () => {
  render(<App />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});

var describe;
var it;
var expect;
var $;
var newEntryForm;

describe("Tests of basic math functions", function () {
  it("Adding 1 should work", function () {
    var foo = 0;
    foo += 1;
    expect(foo).toEqual(1);
  });

  it("Subtracting 1 should work", function () {
    var foo = 0;
    foo -= 1;
    expect(foo).toEqual(-1);
  });




});


/**Tests correct object behavior looking for valid responses when new values are submitted and a message is liked or disliked.
*/
describe("Test for correct object behavior", function () {

  it("Like button should add a like", function () {

    $("#likebtn value=5").click();
    expect(Response).toEqual(200);
  });

  it("Dislike button should add a dislike", function () {

    $("#dislikebtn value=5").click();
    expect(Response).toEqual(200);
  });


  it("Clear form should clear", function () {
    //$("#addCancel").click();
    newEntryForm.clearForm();
    expect($("#newTitle").val()).toEqual("");
    expect($("#newMessage").val()).toEqual("");
  });

  it("Add message should add row", function () {
    $("#newTitle").val().setValue("test");
    $("#newMessage").val().setValue("test");
    $("##add-button").click();
    expect(Response).toEqual(200);
  });

  it("Add comment should add row", function () {
    $("#add-comment-button").val().setValue("test");
    $("#add-button").click();
    // reset the UI, so we don't mess up the next test
    $('#onClick').click();
    expect(Response).toEqual(200);
  });

  it("UI Test: cancel Button returns to showElements", function () {
    // click the button for showing the add button
    $('#showFormButton').click();

    // press cancel
    $('#addCancel').click();

    // expect that the add form is hidden
    expect($("##add-button").attr("style").indexOf("display: none;")).toEqual(0);

    // expect that the element listing is there
    expect($("#showFormButton").attr("style").indexOf("display: none;")).toEqual(-1);
  })

});
