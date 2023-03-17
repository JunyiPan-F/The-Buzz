// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.

var $: any;

// The 'this' keyword does not behave in JavaScript/TypeScript like it does in
// Java.  Since there is only one NewEntryForm, we will save it to a global, so
// that we can reference it from methods of the NewEntryForm in situations where
// 'this' won't work correctly.
var newEntryForm: NewEntryForm;

// This constant indicates the path to our backend server (change to your own)
const backendUrl = "https://thebuzzaws.herokuapp.com";



/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
class NewEntryForm {
    /**
     * To initialize the object, we say what method of NewEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    constructor() {
        $("#addCancel").click(this.clearForm);
        $("#addButton").click(this.submitForm);
    }

    /**
     * Clear the form's input fields
     */
    clearForm() {
        $("#newTitle").val("");
        $("#newMessage").val("");
        // reset the UI
        $("#addElement").hide();
        //$("#editElement").hide();
        $("#showElements").show();
    }

    /**
     * Check if the input fields are both valid, and if so, do an fetch call.
     * Then the fetch performs a post to add the input data to the database
     */

        
     submitForm() {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let title = "" + $("#newTitle").val();
        let msg = "" + $("#newMessage").val();
        if (title === "" || msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        
        //Form is cleared after this POST is performed
        fetch( backendUrl + "/messages", {
            method: "POST",
            body: JSON.stringify(
                {
                    mTitle: title,
                    mMessage: msg,
                    mLikeCount: 0,
                    mDislikeCount: 0
                }
            ),
            headers:{
                "Content-type": "application/json"
            }
        })
        .then(response => response.text())
        .then(data=>console.log(data))
        .then(newEntryForm.clearForm)
    }

    /**
     * This is not used
     * 
     * @param data The object returned by the server
     *
    private onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form
        if (data.mStatus === "ok") {
            newEntryForm.clearForm();
        }

        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }
    */
} // end class NewEntryForm

// a global for the main ElementList of the program.  See newEntryForm for 
// explanation
var mainList: ElementList;


/**
 * ElementList provides a way of seeing all of the data stored on the server.
 */

/**This is the first GET method. This will get all the needed information from the data base and display it to the screen. */
class ElementList {
    /**
     * refresh is the public method for updating messageList
     */
    refresh() {
        // Issue a GET, and then pass the result to update()
        
        //AJAX GET performed to obtain data from database
        $.ajax({
            type: "GET",
            url: backendUrl + "/messages",
            dataType: "json",
            success: mainList.update
        });
        

    }

    /**
     * update is the private method used by refresh() to update messageList
     */
    private update(data: any) {
        $("#messageList").html("<table>");
        for (let i = 0; i < data.mData.length; ++i) {
            $("#messageList").append("<tr> <td width = '100px'>"+ data.mData[i].mTitle + "</td><td width = '400px'>" +data.mData[i].mContent + "</td><td width = '50px'>" + data.mData[i].mLikeCount + "</td><td width = '50px'>" + data.mData[i].mDislikeCount +"</td><td width = '50px'>" + mainList.buttons(data.mData[i].mId) + "</td></tr>" );
        }
        $("#messageList").append("</table>");
        // Find all of the Like buttons, and set their behavior
            $(".likebtn").click(mainList.clickLike);
        // Find all of the Dislike buttons, and set their behavior
            $(".dislikebtn").click(mainList.clickDislike);        
    }

    
    
    /** 
     * clickLike is the code we run in response to a click of a Like button
    */
    private clickLike() {
        // This will increment the likes of the given place utilizing the put
        let id = $(this).data("value");
        fetch("/messages/" + id + "/0", {
            method: "PUT",
            body: JSON.stringify(
                {
                    likes_or_dislikes: 0  
                }
            ),
            headers:{
                "Content-type": "application/json"
            }
        })
            .then(response => response.text())
            .then(data=>console.log(data))
    }

    /** 
     * clickDislike is the code we run in response to a click of a dislike button
    */
     private clickDislike() {
        // This will increment the dislikes of the given place
        let id = $(this).data("value");
        fetch("/messages/" + id + "/1", {
            method: "PUT",
            body: JSON.stringify(
                {
                    likes_or_dislikes: 1 
                }
            ),
            headers:{
                "Content-type": "application/json"
            }
        })
            .then(response => response.text())
            .then(data=>console.log(data))
    }
    

    /**
    * clickDelete is the code we run in response to a click of a delete button
    
    private clickDelete() {
        // for now, just print the ID that goes along with the data in the row
        // whose "delete" button was clicked
        let id = $(this).data("value");
        $.ajax({
            type: "DELETE",
            url: backendUrl + "/messages/" + id,
            dataType: "json",
            // TODO: we should really have a function that looks at the return
            //       value and possibly prints an error message.
        success: mainList.refresh
        })
    }

    */


    /**
    * SET up buttons so that when they are clicked they like or dislike the likes value and increment it. Have them change then reset the likes/dislikes value
    */
    private buttons(id: string): string {
        return "<td><button class='likebtn' data-value='" + id
        + "'>Like</button><button class='dislikebtn' data-value='" + id
        + "'>Dislike</button></td>";
    }


    /*
    $.ajax({
        type: "POST",
        url: backendUrl + "/messages",
        dataType: "json",
        data: JSON.stringify({ mTitle: title, mMessage: msg, like_or_dislike: 0 }),
        success: newEntryForm.onSubmitResponse
    });
    */
    //I know that I need these two things. I also know that it will be some combination of a POST and other
    
} // end class ElementList

    //Ultimately how the app starts running once it is ready

    $(document).ready(function () {
        // Create the object that controls the "New Entry" form
        newEntryForm = new NewEntryForm();
        // Create the object for the main data list, and populate it with data from
        // the server

        //Creates the list that then gets sent to refresh where it is populated with the neccessary values
        mainList = new ElementList();
        mainList.refresh();
        // set up initial UI state
        $("#addElement").hide();
        $("#showElements").show();

        // set up the "Add Message" button
        $("#showFormButton").click(function () {
        $("#addElement").show();
        $("#showElements").hide();
        });
    });

