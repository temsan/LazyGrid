package com.example.application.views.main;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import com.vaadin.flow.component.html.H2;
import com.example.application.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


@AnonymousAllowed
@PageTitle("Dialog")
@Route(value = "dialog", layout = MainLayout.class)
public class DialogView extends VerticalLayout {

  public DialogView() {
    setClassName("page-dialog");
    setSizeFull();
    
    Dialog dialog = new Dialog();
    dialog.setWidth("800px");
    dialog.setHeight("400px");

    var lay = new Modal();
    dialog.add(lay);

    // var lay = new Modal(dialog);
    // dialog.add(lay);

    // dialog.add(DialogLayout(dialog));
    dialog.addDialogCloseActionListener((e) -> {
      System.out.println("CLOSE");
      dialog.close();
    });



    Button button = new Button("OPEN", event -> {
      dialog.open();
    });
    button.addThemeVariants(ButtonVariant.LUMO_SMALL);
    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    var title = new H2("Dialog");
    title.setClassName("title");
    add(title, button);
  }


  // private static VerticalLayout DialogLayout(Dialog dialog) {
  //   var title = new H2("Page Modal");
  //   VerticalLayout layout = new VerticalLayout(title);

  //   return layout;
  // }
}
