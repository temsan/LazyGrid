package com.example.application.views.users;

import com.example.application.data.entity.Users;
import com.example.application.data.service.UsersRepository;
import com.example.application.layouts.MainLayout;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.data.domain.PageRequest;

@AnonymousAllowed
@PageTitle("Users")
@Route(value = "users", layout = MainLayout.class)
public class ListUsers extends VerticalLayout {
  
  public ListUsers(UsersRepository service) {
    setSizeFull();

    var grid = new Grid<>(Users.class);
    grid.setSelectionMode(SelectionMode.SINGLE);
    grid.removeAllColumns();
    ///////////////////////
    grid.setItems(query -> {
      return service.findAll(
        PageRequest.of(query.getPage(), query.getPageSize())
      ).stream();
    });
    ///////////////////////
    grid.addColumn("username")
    .setHeader("USERNAME");
    ///////////////////////

    grid.addItemClickListener(item -> {
      Users model = item.getItem();

      Dialog dialog = new Dialog();
      dialog.setWidth("800px");
      dialog.setHeight("400px");
      dialog.add(new PageUsers(model));
      dialog.setCloseOnEsc(true); 
      dialog.open();
    });

    H3 title = new H3("USERS");
    add(title, grid);
  }
}
