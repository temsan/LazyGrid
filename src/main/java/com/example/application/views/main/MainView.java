package com.example.application.views.main;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.binder.ValidationException;

import com.vaadin.flow.data.binder.Binder;
import com.example.application.data.entity.Persons;
import com.example.application.data.service.PersonsRepository;
import com.example.application.layouts.MainLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import jakarta.annotation.security.RolesAllowed;

import com.example.application.security.SecurityService;

@AnonymousAllowed
@PageTitle("Main")
@Route(value = "main", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class MainView extends HorizontalLayout {

    private Persons record;
    private SecurityService security;

    public MainView(PersonsRepository model, SecurityService security) {
        this.security = security;
        // UserDetails authUser = security.getAuthenticatedUser();

        // authUser.getAuthorities();

        setSizeFull();
        setSpacing(false);
        VerticalLayout left = new VerticalLayout();
        VerticalLayout right = new VerticalLayout();

        // left.add(new H2(authUser.getUsername()));
        Binder<Persons> binder = new Binder<>(Persons.class);

        /////////////////////////////////////
        var leftTitle = new H2("PostgreSQL");

        var grid = new Grid<>(Persons.class);
        grid.setItems(query -> { 
            return model.findAll(
                PageRequest.of(query.getPage(), query.getPageSize())
            ).stream(); 
        });

        grid.addItemClickListener(event -> { 
            this.record = event.getItem();
            binder.readBean(this.record);
        });

        left.setWidth("70%");
        left.add(leftTitle, grid);
        /////////////////////////////////////


        /////////////////////////////////////
        var rightTitle = new H2("Form");
        right.setWidth("calc(30% - 16px)");

        TextField name = new TextField();
        name.setLabel("Name");
        name.setWidth("100%");

        binder.bind(
            name,
            Persons::getName,
            Persons::setName
        );


        right.add(rightTitle, name);
        /////////////////////////////////////

        /////////////////////////////////////
        var record = new Button("UPDATE");
        record.addClickListener(event -> {
            var mode = grid.getSelectionModel();
            var selected = mode.getFirstSelectedItem();

            if(!selected.isEmpty()) {
                try {
                    binder.writeBean(this.record); model.save(this.record);
                    grid.getDataProvider().refreshItem(this.record);
                } catch (ValidationException e) {
                    
                }
            }
        });

        var space = new VerticalLayout();
        space.setHeightFull();

        var footer = new VerticalLayout();
        footer.setJustifyContentMode(JustifyContentMode.END);
        footer.setAlignItems(Alignment.END);
        footer.add(record);

        right.add(space, footer);
        /////////////////////////////////////


        add(left, right);
    }

}
