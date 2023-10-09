package com.example.application.views.users;

import com.vaadin.flow.server.auth.AnonymousAllowed;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.example.application.data.entity.Users;

@AnonymousAllowed
@PageTitle("PageUsers")
public class PageUsers extends VerticalLayout {

  public PageUsers(Users model)  {
    H3 title = new H3(model.getUsername());
    add(title);
  }

}
