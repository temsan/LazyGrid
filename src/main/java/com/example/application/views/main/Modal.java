package com.example.application.views.main;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.example.application.layouts.MainLayout;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@AnonymousAllowed
@PageTitle("Modal")
@Route(value = "modal", layout = MainLayout.class)
public class Modal extends VerticalLayout {

  public Modal()  {
    var title = new H2("Page Modal");
    add(title);
  }
  
}
