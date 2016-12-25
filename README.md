
<h1>Main components</h1>
In app “auction system” we have admin and simple user. Admin can`t be registered. Only admin can apply user item to bid and also admin cannot offer new price to the item. When user add his product to the app, this product goes to his unchecked list of products and he have to wait while admin approves his item.

When admin approve item, it goes to the list of bidding items. This list is the same for all users and admin. Users can offer new price to this item, but the price must be bigger than previous. User who create this item can sell it, simply pressed on button “Sell off” (only if someone has done bet on it) after that this item goes to the winner list user that offered the biggest price.
Admin can delete user item when they are in unchecked list.

There is a user bot in this app. Bot creates new product or bid on random item (50 to 50) every 10 sec (You can make your own time in BotService.TIMEBOT) This is done in service, and when bot completes some action, Broadcast is casted, so if activity is open (ListOfProductActivity or ProductActivity) it can receive the update and handle it properly – update the UI.


<h1>Screenshots</h1>
Here we can see first login screen – we can register or login here. Also, we have hardcoded admin user, who can monitor (accept/reject) products
<p align="center">
  <img src="https://github.com/DmitryKizama/Auction-System/blob/master/screenshots/1.png" width="350"/>
  <img src="https://github.com/DmitryKizama/Auction-System/blob/master/screenshots/2.png" width="350"/>
</p>
Lets create a user, and register.
After that, let’s add new Product – we add name and cost, and upload a photo form gallery/camera
<p align="center">
  <img src="https://github.com/DmitryKizama/Auction-System/blob/master/screenshots/3.png" width="350"/>
  <img src="https://github.com/DmitryKizama/Auction-System/blob/master/screenshots/4.png" width="350"/>
</p>
And press “Add”
The product would be displayed in first tab – your unregistered products, since admin still did not approved it.
<p align="center">
  <img src="https://github.com/DmitryKizama/Auction-System/blob/master/screenshots/5.png" width="350"/>
</p>
On second tab we can see list of all available for bidding products (all approved products)
<p align="center">
  <img src="https://github.com/DmitryKizama/Auction-System/blob/master/screenshots/6.png" width="350"/>
</p>
Let’s make a bid  on a product :
<p align="center">
  <img src="https://github.com/DmitryKizama/Auction-System/blob/master/screenshots/7.png" width="350"/>
</p>
Also, we can login as admin (login “admin”, password “admin”) and confirm on 3rd tab the product “car” we created at the beginning
<p align="center">
  <img src="https://github.com/DmitryKizama/Auction-System/blob/master/screenshots/8.png" width="350"/>
  <img src="https://github.com/DmitryKizama/Auction-System/blob/master/screenshots/9.png" width="350"/>
</p>

Also, if someone bids the car, we can sell it, and it will appear in won tab of our buyer. 

<h1>Technologies and design patterns used.</h1>

Features, implemented in application: <br>
1.	RecyclerView used for showing all list of products<br>
2.	Green Dao library for database
I used to have Active Android ORM, but it is deprecated now<br>
Green Dao build on SQLite, but it is safer than writing it by myself. <br>
3.	UniversalImageLoader for photo loading and caching 
I always use this awesome lib, since it can cache in memory and disc, and load photos as from internet and as from disc. So, add server support to app in future would be easier, since I load all (local) photos using UIL.
