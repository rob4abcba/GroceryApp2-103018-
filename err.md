
margp (ABND) [Nov 3rd at 6:50 PM]
https://github.com/margp2/GroceryApp2-103018-/tree/master/GroceryApp2-master/GroceryApp1(110318)
GitHub
margp2/GroceryApp2-103018-
GroceryApp2. Contribute to margp2/GroceryApp2-103018- development by creating an account on GitHub.

i am almost close to being done
i think i am stuck in some places but can't figure out why
my increment decrements don't work
i also made my sale button focusable = false and it still shows up!
:disappointed: Please help!!!!
GitHub
margp2/GroceryApp2-103018-
GroceryApp2. Contribute to margp2/GroceryApp2-103018- development by creating an account on GitHub.
 
(edited)


17 replies
thebkline[ABND] [24 hours ago]
Good morning @margp (ABND).  First, focusable doesn't hide the button.  The purpose of focusable is to allow you to click on the ListView item or the Button.  When focusable is set to true, the button is the only thing that you can click.  https://stackoverflow.com/a/36986338

Second, when I attempted to click on an item in the ListView, your app crashed.  You were using GetLoaderManager and setting the callback parameter to null.  When you invoke initLoader on line 79 in EditorActivity, you need to use GetSupportLoaderManager since you're using the support library version of LoaderManager.  https://stackoverflow.com/a/42154589

Third, your increment button clears the text on qtyEditText for some reason.  I'm not sure if you were just testing something?  The decrement button does decrement the value in your variable named quantity.  However, I don't see where you retrieve the current value in the EditText.  For example, when I clicked on the item in the ListView, the text in qtyEditText was set to 10.  When I clicked the decrement button, the variable named quantity is 0 so it gets decremented to -1 and I get a Toast message.  It looks like in your increment and decrement setOnClickListener method, you were retrieving the value from the EditText but it is currently commented out.  My suggestion would be to get the current value from qtyEditText before incrementing or decrementing.   Hopefully, this gets you on the right track to make the necessary changes.
Stack Overflow
Focusable Button in Listview
For a ListView with custom row layout like this: <?xml version="1.0" encoding="utf-8"?> <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android" android:orientation="horiz...
 
 Stack Overflow
LoaderManager does not accept 'this'
Okay, I surrender. I cannot figure it out. I'm following a Udacity course on Android Basics and need to figure out how to load data using a Loader. However, when I use the following line, the 'thi...
 

margp (ABND) [24 hours ago]
@thebkline[ABND] thank you for the reply, I am having a hard time following through the increment/decrement logic. I am not sure why decrement button keeps going below 0 (any suggestions as to how i can improve those methods?).  I will try to use the supportloadermanager which is what i was using before.  Thanks for telling me about the button thing i will look into making it invisible.

thebkline[ABND] [23 hours ago]
@margp (ABND), the issue with your increment and decrement counter is that you are not retrieving the current quantity.  If we focus on just the mDecreaseQtyBtn.setOnClickListener method, the first thing you do is at line 111 which is `mQuantityEditText.setText ( String.valueOf ( quantity-- ) );`  You commented out the statement at line 110 which retrieved the current value.  However, the variable named `quantity` does not contain the current text value of `qtyEditText` because you never used `getText` to retrieve it.  So, when the `onClickListener` method runs, the variable named `quantity` is 0 and you end up decrementing 0 by 1 which gives you -1.  You need to make sure that before you increment or decrement, you are retrieving the current quantity first.  Then perform your increment or decrement and set the EditText to the new value.  I see that you do the decrement or increment inside the setText.  I would recommend separating those into two different statements so you would have:
`quantity -= 1;`
`mQuantityEditText.setText ( String.valueOf ( quantity ) );`
I know it's extra code but for me, I don't mind it.  Once you get your decrement working, you should be able to duplicate the code for your increment.  Does that make sense?  If not, let me know and I can try to explain it differently or with actual code examples. (edited)

margp (ABND) [20 hours ago]
Here is what i did:   mIncreaseQtyBtn.setOnClickListener ( new View.OnClickListener () {
          @Override
          public void onClick(View v) {
              int quantity = Integer.parseInt ( mQuantityEditText.getText ().toString ().trim () );
              if (quantity >= 0){
                mQuantityEditText.setText ( String.valueOf ( ++quantity ) );
              }
          }
      } );
       mDecreaseQtyBtn.setOnClickListener ( new View.OnClickListener () {
           @Override
           public void onClick(View v) {
            int quantity = Integer.parseInt ( mQuantityEditText.getText ().toString ().trim () );
                if (quantity <= 0 ){
                    Toast.makeText ( EditorActivity.this, "The quantity can not be less than 0", Toast.LENGTH_SHORT ).show ();
                } else {mQuantityEditText.setText ( String.valueOf ( --quantity ) );}
           }
       } );

Which seems to work (edited)

margp (ABND) [20 hours ago]
Pasted image at 2018-11-04, 12:17 PM


margp (ABND) [20 hours ago]
but idk why this keeps happening when I put in the information from my editor activity (edited)

thebkline[ABND] [20 hours ago]
I'm glad you got your increment and decrement logic working.  I am not sure what you mean when you say you don't know why this keeps happening.  What is happening specifically?

thebkline[ABND] [20 hours ago]
Unfortunately if you were pointing at something with your mouse, it doesn't show in the screen shot.  Are you referencing the text in the ListView or the Button that is all grey in the TextView?  Or something else?

margp (ABND) [20 hours ago]
For example, I tried to save this information here.
Pasted image at 2018-11-04, 12:28 PM


margp (ABND) [20 hours ago]
But after saving it looks like this, and i receive a toast saying please fill in all the required fields even though i did.
Pasted image at 2018-11-04, 12:30 PM


margp (ABND) [20 hours ago]
I hope this helps :confused:

thebkline[ABND] [20 hours ago]
OK.  I understand that part of the issue is that you get a Toast message that says you need to fill in the required information and you shouldn't get a Toast message.  The second part of your issue is what?  You don't like the fact that the text is all smashed together?  There's information missing in the ListView after you save?  You don't know why the button is smashed against the text?  You don't know why the button is all grey with no text?  I just need a little more clarification just so I know what to concentrate on so I can try to help.  Also, if you've updated your code since I last downloaded a zip file from your GitHub, would you please commit and push to GitHub?  If it's just minor things like you fixing your increment and decrement, don't worry about it.  I'll just use what I downloaded last. (edited)

margp (ABND) [20 hours ago]
@thebkline[ABND] yes, all of those that you described above are my issues including the fact that the supplier information won't display either. Yes, let me upload the new code.

thebkline[ABND] [20 hours ago]
OK.  Thank you for the clarification.  That definitely helps.  Let me know when your latest version is on GitHub please.

margp (ABND) [19 hours ago]
https://github.com/margp2/GroceryApp2-103018-/tree/master/GroceryApp2-master/GroceryApp1(110418) It's this one! Thank you for understanding my concerns! :slightly_smiling_face:
GitHub
margp2/GroceryApp2-103018-
GroceryApp2. Contribute to margp2/GroceryApp2-103018- development by creating an account on GitHub.
 

thebkline[ABND] [19 hours ago]
I will take a look, see what I can figure out, and get back to you.

thebkline[ABND] [19 hours ago]
@margp (ABND) First, the easy one...your Toast message.  The reason you get a Toast message is because it is outside your IF statement.  Therefore, your Toast message is not conditioned so it always shows.

Second, your text and button in your `list_item` layout is all smashed together because there are no margins or padding or anything to tell it how to layout your TextViews and button.  You have 16dp of padding set in the LinearLayout but that only affects the LinearLayout.  Not the TextViews and Button contained in the LinearLayout.

Third, your Button has no text because in your `list_item` layout you don't have a `text` attribute.  Unless you are setting it somewhere else and I'm not seeing it in your code.

Fourth, the reason your supplier information is missing in your ListView is because in your `bindView` method of your `GroceryCursorAdapter` class, you are only retrieving the name, price, and quantity.

Hopefully, this addresses all your issues.  If I have misunderstood something or have not addressed something or you need a bit more explanation on something, please let me know.
