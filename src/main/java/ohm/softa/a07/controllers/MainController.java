package ohm.softa.a07.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import ohm.softa.a07.api.OpenMensaAPI;
import ohm.softa.a07.model.Meal;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {

	private static final String baseUrl = "https://openmensa.org/api/v2/";
	private static Gson gson = new GsonBuilder().create();
	private Retrofit retrofit = new Retrofit.Builder()
		.baseUrl(baseUrl)
		.addConverterFactory(GsonConverterFactory.create(gson))
		.build();

	private OpenMensaAPI openMensaAPI = retrofit.create(OpenMensaAPI.class);

	// use annotation to tie to component in XML
	@FXML
	private Button btnRefresh;

	@FXML
	private Button btnClose;

	@FXML
	private CheckBox chkVegetarian;

	@FXML
	private void onCloseClicked(ActionEvent event){
			System.out.println("Close was called");
			Platform.exit();
			System.exit(0);
	}

	@FXML
	private ListView<String> mealsList;

	@FXML
	private ObservableList<String> observableList = FXCollections.observableArrayList();

	private List<Meal> loadMeals() {
		Call<List<Meal>> meals = openMensaAPI.getMeals("2019-05-16");
		try {
			List<Meal> mealsRes = meals.execute().body();
			return mealsRes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	private String getToday(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String today = sdf.format(new Date());
		return today;
	}

	@FXML
	void onRefreshClicked(ActionEvent event){
		if (chkVegetarian.isSelected()) {
			System.out.println("vegetarian filter was set");
		} else {
			System.out.println("vegetarian filter was unset");
		}
		observableList.add("hallo");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mealsList.setItems(observableList);
	}
}
