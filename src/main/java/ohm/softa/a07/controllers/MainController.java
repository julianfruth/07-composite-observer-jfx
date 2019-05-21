package ohm.softa.a07.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import ohm.softa.a07.api.OpenMensaAPI;
import ohm.softa.a07.model.Meal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
	private ListView<String> mealsList;
	@FXML
	private ObservableList<String> observableList = FXCollections.observableArrayList();

	@FXML
	private void onCloseClicked(ActionEvent event) {
		System.out.println("Close was called");
		Platform.exit();
		System.exit(0);
	}

	private String getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String today = sdf.format(new Date());
		return today;
	}

	@FXML
	void onRefreshClicked(ActionEvent event) {
		Call<List<Meal>> req = openMensaAPI.getMeals(getToday());
		req.enqueue(new Callback<List<Meal>>() {
			@Override
			public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
				if (chkVegetarian.isSelected()) {
					observableList
						.setAll(response.body()
							.stream()
							.filter(i -> i.getCategory().toLowerCase().equals("vegetarisch"))
							.map(i -> i.toString())
							.collect(Collectors.toCollection(ArrayList::new)));
					System.out.println("vegetarian filter was set");
				} else {
					observableList
						.setAll(response.body()
							.stream()
							.map(i -> i.toString())
							.collect(Collectors.toCollection(ArrayList::new)));
					System.out.println("vegetarian filter was unset");
				}
			}

			@Override
			public void onFailure(Call<List<Meal>> call, Throwable t) {

			}
		});


	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mealsList.setItems(observableList);
	}
}
