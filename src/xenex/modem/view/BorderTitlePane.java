/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xenex.modem.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
class BorderTitlePane extends StackPane {
    BorderTitlePane(String titleString, Node content) {
      Label title = new Label("  " + titleString + "  ");
      title.getStyleClass().add("border-title-title");
      StackPane.setAlignment(title, Pos.TOP_LEFT);

      StackPane contentPane = new StackPane();
      content.getStyleClass().add("border-title-content");
      contentPane.getChildren().add(content);

      getStyleClass().add("border-title-border");
      getChildren().addAll(title, contentPane);
    }
  }