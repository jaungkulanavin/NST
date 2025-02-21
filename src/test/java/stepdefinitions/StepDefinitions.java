package stepdefinitions;

import com.microsoft.playwright.*;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.Map;

public class StepDefinitions {

	private Page page;
	private String productID;
	private String itemText;

	@Before
	public void setUp() {
		page = PlaywrightSingleton.getInstance().getPage();
	}

	@Given("Open the {string} website")
	public void openWebsite(String title) {
		page.navigate("https://petstore.octoperf.com/actions/Catalog.action");
		String pageTitle = page.title();
		if (!pageTitle.equals(title)) {
			throw new RuntimeException("Expected title to be " + title + " but found " + pageTitle);
		}
	}

	@Given("Navigate to the registration page")
	public void navigateToRegistrationPage() {
		Locator signInButton = page.locator("//a[contains(@href, 'orm') and text()='Sign In']");
		signInButton.waitFor();
		signInButton.click();
		Locator registerNowButton = page.locator("//a[contains(@href, 'new') and text() = 'Register Now!']");
		registerNowButton.waitFor();
		registerNowButton.click();
		Locator saveNewAccountButton = page.locator("//input[@value='Save Account Information']");
		saveNewAccountButton.waitFor();
		if (!signInButton.isVisible()) {
			throw new RuntimeException("Sign In button is not visible");
		}
	}

	@Then("Fill in the registration form with the following details:")
	public void fillInRegistrationForm(DataTable dataTable) {
		List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
		Map<String, String> row = data.get(0);
		page.fill("//input[@name='username']", row.get("User_ID"));
		page.fill("//input[@name='password']", row.get("Password"));
		page.fill("//input[@name='repeatedPassword']", row.get("Password"));
		page.fill("//input[@name='account.firstName']", row.get("First_name"));
		page.fill("//input[@name='account.lastName']", row.get("Last_name"));
		page.fill("//input[contains(@name, 'account.email')]", row.get("Email"));
		page.fill("//input[@name='account.phone']", row.get("Phone"));
		page.fill("//input[@name='account.address1']", row.get("Address_1"));
		page.fill("//input[@name='account.address2']", row.get("Address_2"));
		page.fill("//input[@name='account.city']", row.get("City"));
		page.fill("//input[@name='account.state']", row.get("State"));
		page.fill("//input[@name='account.zip']", row.get("Zip"));
		page.fill("//input[@name='account.country']", row.get("Country"));

		page.selectOption("//select[contains(@name, 'language')]", row.get("Language"));
		page.selectOption("//select[contains(@name, 'account.favouriteCategoryId')]", row.get("Category"));

		if ("true".equals(row.get("MyList"))) {
			page.check("//input[@name='account.listOption']");
		}

		if ("true".equals(row.get("Banner"))) {
			page.check("//input[@name='account.bannerOption']");
		}

	}

	@And("Submit the registration form")
	public void submitTheRegistrationForm() {
		Locator submitButton = page.locator("//input[@value='Save Account Information']");
		submitButton.waitFor();
		submitButton.click();

		Locator homePageElement = page.locator("//a[contains(@href, 'orm') and text()='Sign In']");
		if (!homePageElement.isVisible()) {
			throw new RuntimeException("Registration failed: Next page not loaded correctly.");
		}
	}

	@When("Login with new account")
	public void loginWithNewAccount(DataTable dataTable) {
		List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
		Map<String, String> row = data.get(0);

		Locator signInButton = page.locator("//a[contains(@href, 'orm') and text()='Sign In']");
		signInButton.waitFor();
		signInButton.click();
		Locator loginButton = page.locator("//input[contains(@value, 'Login')]");
		if (!loginButton.isVisible()) {
			throw new RuntimeException("Failed: Next page not loaded correctly.");
		}
		page.fill("//input[@name='username']", row.get("User_ID"));
		page.fill("//input[@name='password']", row.get("Password"));
		loginButton.waitFor();
		loginButton.click();


	}

	@Then("Home page after login")
	public void homePageAfterLogin() {

		Locator myAccountButton = page.locator("//a[contains(@href, '/actions/Account.action?editAccountForm=')]");
		if (!myAccountButton.isVisible()) {
			throw new RuntimeException("Failed: Next page not loaded correctly.");
		}
	}

	@When("Successful login")
	public void successfulLogin(DataTable dataTable) {
		List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
		Map<String, String> row = data.get(0);

		Locator signInButton = page.locator("//a[contains(@href, 'orm') and text()='Sign In']");
		signInButton.waitFor();
		signInButton.click();
		Locator loginButton = page.locator("//input[contains(@value, 'Login')]");
		if (!loginButton.isVisible()) {
			throw new RuntimeException("Failed: Next page not loaded correctly.");
		}
		page.fill("//input[@name='username']", row.get("User_ID"));
		page.fill("//input[@name='password']", row.get("Password"));
		loginButton.waitFor();
		loginButton.click();
	}

	@Given("Catalog page")
	public void catalogPage() {
		Locator myAccountButton = page.locator("//a[contains(@href, '/actions/Account.action?editAccountForm=')]");
		myAccountButton.waitFor();
		if (!myAccountButton.isVisible()) {
			throw new RuntimeException("Failed: Next page not loaded correctly.");
		}
	}

	@When("Search for product {string}")
	public void searchForProduct(String keyword) {
		Locator searchBox = page.locator("//input[@name='keyword']");
		searchBox.waitFor();
		if (!searchBox.isVisible()) {
			throw new RuntimeException("Failed: Next page not loaded correctly.");
		}
		page.fill("//input[@size='14']", keyword);
		Locator searchButton = page.locator("//input[contains(@value, 'Search')]");
		searchButton.waitFor();
		searchButton.click();
	}


	@Then("Validate search results {string}")
	public void validateSearchResultsAndProductAvailability(String keyword) {
		Locator checkSearch = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > table > tbody > tr:nth-of-type(2) > td:nth-of-type(3)");
		if(!checkSearch.isVisible()){
			throw new RuntimeException("Failed: keyword " + keyword + " not found in database");
		}
		Locator rowsSearch = page.locator("table > tbody > tr");
		int rowCount = rowsSearch.count();
		rowCount = rowCount - 2; /*Delete header and bottom row*/
		System.out.println("Number of rows in the table: " + rowCount);
		rowCount = rowCount + 1; /*Add bottom row to make it count all except the header*/

		for (int i = 1; i < rowCount; i++) {
			Locator column = rowsSearch.nth(i).locator("td:nth-child(3)");

			String columnText = column.textContent();

			if (columnText.toLowerCase().contains(keyword.toLowerCase())) {
				System.out.println("Keyword found in row " + (i + 1) + ": " + columnText);
			} else {

				System.out.println("Keyword not found in row " + (i + 1) + ": " + columnText);
				throw new RuntimeException("Keyword not found in row " + (i + 1) + ": " + columnText);

			}

		}
	}

	@And("Validate product availability")
	public void validateProductAvailability() {
		Locator searchItem = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > table > tbody > tr:nth-of-type(2) > td:nth-of-type(1) > a");
		searchItem.waitFor();
		searchItem.waitFor();
		if(!searchItem.isVisible()){
			throw new RuntimeException("Failed: keyword not found in database");
		}
		searchItem.click();

		Locator itemID = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > table > tbody > tr:nth-of-type(2) > td:nth-of-type(1) > a");
		itemID.waitFor();
		if(!itemID.isVisible()){
			throw new RuntimeException("Failed: Can be searched but can not found in product page");
		}
		itemID.click();

		Locator productAvailability = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > table > tbody > tr:nth-of-type(5) > td");
		productAvailability.waitFor();
		Locator productName = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > table > tbody > tr:nth-of-type(4) > td");

		String productText = productName.textContent();
		String columnText = productAvailability.textContent();
		System.out.println(productText + " status: " + columnText);
	}


	@When("User select item from the side menu")
	public void userSelectItemFromTheSideMenu() {
		Locator sideMenu1 = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > div:nth-of-type(1) > div > a:nth-of-type(1) > img");
		sideMenu1.waitFor();
		if(!sideMenu1.isVisible())
		{
			throw new RuntimeException("Failed: Fish on the side menu has disappeared");
		}
		sideMenu1.click();

		Locator selectFirstOption = page.locator("table > tbody > tr:nth-of-type(2) > td:nth-of-type(1)");
		selectFirstOption.waitFor();

		if(!selectFirstOption.isVisible()){
			throw new RuntimeException("Failed: No item available.");
		} else {
			productID = selectFirstOption.textContent();
			System.out.println("Select product ID: " + productID);
		}
		selectFirstOption.click();

	}

	@And("User add item to the cart")
	public void userAddItemToTheCart() {
		Locator addtocartButton = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > table > tbody > tr:nth-of-type(2) > td:nth-of-type(5) > a");
		addtocartButton.waitFor();
		if(!addtocartButton.isVisible()){
			throw new RuntimeException("Failed: No 'Add to Cart' button");
		} else {
			Locator selectFirstproduct = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > table > tbody > tr:nth-of-type(2) > td:nth-of-type(2)");
			String productIDSelect = selectFirstproduct.textContent();
			if (!productIDSelect.equals(productID)){
				throw new RuntimeException("Failed: Text does not match. Expected: " + productID + " but found: " + productIDSelect);
			} else {
				Locator itemSelect = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > table > tbody > tr:nth-of-type(2) > td:nth-of-type(1)");
				itemText = itemSelect.textContent();
				System.out.println("Select item ID: " + itemText);
			}
		}

		addtocartButton.click();
	}

	@Then("Selected item on the cart")
	public void selectedItemOnTheCart() {
		Locator cartPage = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > div:nth-of-type(1) > h2");
		cartPage.waitFor();
		if(!cartPage.isVisible()){
			throw new RuntimeException("Failed: No 'Shopping Cart' button");
		}
		String cartPageText = cartPage.textContent();
		String expectedText = "Shopping Cart";
		if (!cartPageText.equals(expectedText)){
			throw new RuntimeException("Failed: Text does not match. Expected: '" + expectedText + "', but found: '" + cartPageText + "'");
		} else {
			System.out.println("Text matches: " + cartPageText);
		}

		Locator itemCart = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > div:nth-of-type(1) > form > table > tbody > tr:nth-of-type(2) > td:nth-of-type(1) > a");
		itemCart.waitFor();
		if(!itemCart.isVisible()){
			throw new RuntimeException("Failed: Selected item " + itemText + "not displayed");
		} else {
			String cartItem = itemCart.textContent();
			if(!cartItem.equals(itemText)){
				throw new RuntimeException("Failed: Selected item " + itemText + " does not match to the item in the cart.");
			}
			System.out.println("Cart Item: " + cartItem);
		}

	}

	@When("User select item from the search")
	public void userSelectItemFromTheSearch() {
		Locator sideMenu1 = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > table > tbody > tr:nth-of-type(2) > td:nth-of-type(1) > a");
		sideMenu1.waitFor();
		if(!sideMenu1.isVisible())
		{
			throw new RuntimeException("Failed: search has disappeared");
		}
		sideMenu1.click();

		Locator selectFirstOption = page.locator("table > tbody > tr:nth-of-type(2) > td:nth-of-type(1)");
		selectFirstOption.waitFor();

		if(!selectFirstOption.isVisible()){
			throw new RuntimeException("Failed: No item available.");
		} else {
			itemText = selectFirstOption.textContent();
			System.out.println("Select item ID: " + itemText);
		}
		selectFirstOption.click();
	}

	@And("User add item from search to the cart")
	public void userAddItemFromSearchToTheCart() {
		Locator addtoCart = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > table > tbody > tr:nth-of-type(7) > td > a");
		addtoCart.waitFor();
		if(!addtoCart.isVisible())
		{
			throw new RuntimeException("Failed: Add to cart button not displayed.");
		} else
		{
			addtoCart.click();

		}
	}

	@Then("Selected latest item on the cart")
	public void selectedLatestItemOnTheCart() {
		Locator cartPage = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > div:nth-of-type(1) > h2");
		cartPage.waitFor();
		if(!cartPage.isVisible()){
			throw new RuntimeException("Failed: No 'Shopping Cart' text");
		}
		String cartPageText = cartPage.textContent();
		String expectedText = "Shopping Cart";
		if (!cartPageText.equals(expectedText)){
			throw new RuntimeException("Failed: Text does not match. Expected: '" + expectedText + "', but found: '" + cartPageText + "'");
		} else {
			System.out.println("Text matches: " + cartPageText);
		}

		Locator rowsSearch = page.locator("table > tbody > tr");
		int rowCount = rowsSearch.count();
		rowCount = rowCount - 2; /*get bottom row*/

		Locator itemCart = rowsSearch.nth(rowCount).locator("td:nth-child(1)");
		itemCart.waitFor();
		if(!itemCart.isVisible()){
			throw new RuntimeException("Failed: Selected item " + itemText + "not displayed");
		} else {
			String cartItem = itemCart.textContent();
			if(!cartItem.equals(itemText)){
				throw new RuntimeException("Failed: Selected item " + itemText + " does not match to the item in the cart.");
			}
			System.out.println("Cart Item: " + cartItem);
		}
	}

	@Given("Shopping cart page")
	public void shoppingCartPage() {
		Locator cartPage = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > div:nth-of-type(1) > h2");
		cartPage.waitFor();
		if(!cartPage.isVisible()){
			throw new RuntimeException("Failed: No 'Shopping Cart' text");
		}
	}

	@Then("The cart has items in list")
	public void theCartHasItemsInList() {
		Locator rowsSearch = page.locator("table > tbody > tr");
		int rowCount = rowsSearch.count();
		rowCount = rowCount - 2; /*Delete header and bottom row*/

		Locator checkItem = rowsSearch.nth(2).locator("td:nth-child(1)"); /*check empty cart*/
		String emptyCart = checkItem.textContent();
		String expectedText = "Your cart is empty";
		if((rowCount == 1) & (!emptyCart.equals(expectedText)))
		{
			System.out.println("Your cart is empty");
			throw new RuntimeException("Your cart is empty");
		} else{

			Locator checkoutBtn = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > div:nth-of-type(1) > a");
			checkoutBtn.waitFor();

			if (!checkoutBtn.isVisible() & (!emptyCart.equals(expectedText))) {
				throw new RuntimeException("Failed: Have " + rowCount + " item on the cart but 'Proceed to Checkout' button not displayed.");
			} else {
				System.out.println("Number of items in the cart: " + rowCount);
			}
		}
	}

	@Then("Validate the items from the cart")
	public void validateTheItemsFromTheCart() {
		Locator romoveBtn = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > div:nth-of-type(1) > form > table > tbody > tr:nth-of-type(2) > td:nth-of-type(8) > a");
		if(!romoveBtn.isVisible())
		{
			System.out.println("Your cart is not empty but 'Remove' is not displayed.");
			throw new RuntimeException("Failed: Your cart is not empty but 'Remove' is not displayed.");
		}
		Locator rowsSearch = page.locator("table > tbody > tr");
		int rowCount = rowsSearch.count();
		rowCount = rowCount - 1; /*Delete header*/

		double subpricelist = 0.0;
		for (int i = 1; i < rowCount; i++) {
			Locator itemCol = rowsSearch.nth(i).locator("td:nth-child(1)");
			Locator quantityCol = rowsSearch.nth(i).locator("td:nth-child(5) > input");
			Locator listpriceCol = rowsSearch.nth(i).locator("td:nth-child(6)");
			Locator totalpriceCol = rowsSearch.nth(i).locator("td:nth-child(7)");

			String column1 = itemCol.textContent();
			String column5 = quantityCol.inputValue();
			int quantity = Integer.parseInt(column5);
			double listPrice = Double.parseDouble(listpriceCol.textContent().replace("$", ""));
			double totalPrice = Double.parseDouble(totalpriceCol.textContent().replace("$", ""));

			boolean isCorrect = (quantity * listPrice) == totalPrice;
			System.out.println("Item: " + column1 + ", Quantity: " + quantity + ", List Price: " + listPrice + ", Total Price: " + totalPrice + ", Calculation Correct: " + isCorrect);
			if (!isCorrect) {
				throw new RuntimeException("Calculation false");
			}
			subpricelist += totalPrice;

		}

		rowCount = rowCount; /*last row*/

		Locator subTotal = rowsSearch.nth(rowCount).locator("td:nth-child(1)");
		double subTotalNum = Double.parseDouble(subTotal.textContent().replace("Sub Total: $", ""));

		boolean isCorrect = subTotalNum == subpricelist;
		System.out.println("Total Price from the list: " + subpricelist + ", Price from the Sub Total: " + subTotalNum + ", match: " + isCorrect);
		if (!isCorrect) {
			throw new RuntimeException("Price from the list is not match to the Sub Total");
		}


	}

	@And("User remove the items from the cart")
	public void userRemoveTheItemsFromTheCart() {
		Locator rowsSearch = page.locator("table > tbody > tr");

		int rowCount = rowsSearch.count();
		rowCount = rowCount - 1; /*Delete header*/

		Locator subTotalBefore = rowsSearch.nth(rowCount).locator("td:nth-child(1)");
		double subTotalNum1 = Double.parseDouble(subTotalBefore.textContent().replace("Sub Total: $", ""));


		for (int i = 1; i < rowCount; i++) {

			Locator itemCol = rowsSearch.nth(1).locator("td:nth-child(1)");
			Locator quantityCol = rowsSearch.nth(1).locator("td:nth-child(5) > input");
			Locator listpriceCol = rowsSearch.nth(1).locator("td:nth-child(6)");
			Locator totalpriceCol = rowsSearch.nth(1).locator("td:nth-child(7)");

			String column1 = itemCol.textContent();
			String column5 = quantityCol.inputValue();
			int quantity = Integer.parseInt(column5);
			double listPrice = Double.parseDouble(listpriceCol.textContent().replace("$", ""));
			double totalPrice = Double.parseDouble(totalpriceCol.textContent().replace("$", ""));

			System.out.println("Remove -> Item: " + column1 + ", Quantity: " + quantity + ", List Price: " + listPrice + ", Total Price: " + totalPrice);
			Locator removeBtn = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > div:nth-of-type(1) > form > table > tbody > tr:nth-of-type(2) > td:nth-of-type(8) > a");
			removeBtn.waitFor();
			removeBtn.click();

			Locator rowsSearch2 = page.locator("table > tbody > tr");
			int rowCountAfterDel = rowsSearch2.count();
			
			String expectedText = "Your cart is empty";
			if((rowCountAfterDel == 3) & ((rowCount - i == 1)))/*(column1.equals(expectedText)))*/
			{
				/*System.out.println("Your cart is empty");*/
				rowCountAfterDel = 1;
			} else{
				rowCountAfterDel -= 1; // Exclude header row
			}



			boolean isRowCorrect = rowCountAfterDel == (rowCount - i);
			System.out.println("Number of items in the cart: " + rowCountAfterDel + ", Row after deleted: " + (rowCount - i) + ", compared: " + isRowCorrect);
			if (!isRowCorrect) {
				throw new RuntimeException("Row not matched");
			}

		}

		Locator checkItem = page.locator("html > body > div:nth-of-type(2) > div:nth-of-type(2) > div:nth-of-type(1) > form > table > tbody > tr:nth-of-type(2) > td > b");
		String emptyCart = checkItem.textContent();
		String expectedText = "Your cart is empty";
		if((!emptyCart.equals(expectedText)))
		{
			System.out.println("Your cart is empty");
		} else {
			throw new RuntimeException("Wording not found");
		}

	}
}
