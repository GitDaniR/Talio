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
package client;

import static com.google.inject.Guice.createInjector;
import java.io.IOException;
import java.net.URISyntaxException;
import client.scenes.*;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var addList = FXML.load(AddListCtrl.class, "client", "scenes", "AddList.fxml");
        var currentBoard = 
            FXML.load(BoardOverviewCtrl.class, "client", "scenes", "BoardOverview.fxml");
        var welcomePage = FXML.load(WelcomePageCtrl.class, "client", "scenes",
                "WelcomePage.fxml");
        var editList = FXML.load(EditListCtrl.class,"client","scenes","EditList.fxml");
        var workspace = FXML.load(WorkspaceCtrl.class , "client", "scenes", "Workspace.fxml");
        var workspaceAdmin = FXML.load(WorkspaceAdminCtrl.class,
                "client", "scenes", "WorkspaceAdmin.fxml");
        var editCard = FXML.load(EditCardCtrl.class,"client","scenes","EditCard.fxml");
        var editBoard = FXML.load(ChangeBoardTitleCtrl.class, "client",
                "scenes","ChangeBoardTitle.fxml");
        var editTag = FXML.load(EditTagCtrl.class,"client","scenes","EditTag.fxml");
        var addTag = FXML.load(AddTagCtrl.class,"client","scenes","AddTag.fxml");
        var tagOverview = FXML.load(TagOverviewCtrl.class,"client","scenes","TagOverview.fxml");
        var addRemoveTags = FXML.load(AddRemoveTagsCtrl.class,"client","scenes","AddRemoveTags.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        mainCtrl.initialize(primaryStage, addList,
                currentBoard, welcomePage, editList, workspace, workspaceAdmin, editCard,
                editBoard, editTag, addTag, tagOverview, addRemoveTags);

    }
}
