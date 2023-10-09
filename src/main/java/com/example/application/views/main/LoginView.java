package com.example.application.views.main;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

  private final LoginForm login = new LoginForm();

  public LoginView() {
    setSizeFull();
    setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);

    login.setAction("login");
    add(login);
  }

  @Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    var query = beforeEnterEvent.getLocation().getQueryParameters();
		if(query.getParameters().containsKey("error")) {
      login.setError(true);
    }
	}

}
