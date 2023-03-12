/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class WelcomePageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField chosenServer;

    @Inject
    public WelcomePageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    // Connects to the server the user inputs in the field "chosenServer"
    public void connectToChosenServer() {
        if(testServerConnection()) {
            server.setServer("http://" + chosenServer.getText() + "/");
            mainCtrl.showBoard();
        }
    }

    // It checks if the server address the user inputs is reachable. If yes, then it returns true.
    public boolean testServerConnection() {
        try {
            URL url = new URL("http://" + chosenServer.getText() + "/api/boards/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            int responseCode = connection.getResponseCode();
            connection.disconnect();

            // If the responseCode == 200 => server is reachable
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}