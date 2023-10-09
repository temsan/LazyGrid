package com.example.application.layouts;

import com.vaadin.flow.component.Tag;



import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;

import java.util.Objects;
import com.vaadin.flow.component.HasElement;

import com.example.application.views.users.ListUsers;
import com.example.application.views.products.ListProducts;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;

@Tag("vaadin-horizontal-layout")
public class MainLayout extends HorizontalLayout implements RouterLayout, BeforeEnterObserver {

  private Tabs tabs = createTabs();

  public VerticalLayout left = new VerticalLayout();
  public VerticalLayout right = new VerticalLayout();

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    tabs.getChildren().forEach(tab -> {
      int index =  tabs.indexOf(((Tab) tab));
      tab.getChildren().forEach(link -> {
        String path = event.getLocation().getPath();
        String href = ((RouterLink) link).getHref();

        if (Objects.equals(href, path)) { 
          tabs.setSelectedIndex(index); return;
        }
      });
    });
  }

  @Override
  public void showRouterLayoutContent(HasElement content) {
    if (content != null) {
      right.add(content.getElement().getComponent()
      .orElseThrow(() -> new IllegalArgumentException("Component")));
    }
  }

  public MainLayout() {
    setClassName("main-layout");
    right.setPadding(false);

    left.add(tabs);
    left.setWidth("350px");
    left.setClassName("main-layout-left");
   
    add(left, right);
  }

  //////////////////////////

  public Tabs createTabs() {
    Tabs tabs = new Tabs();
    tabs.add(
      createTab(VaadinIcon.PADDING_BOTTOM, "USERS", ListUsers.class),
      createTab(VaadinIcon.PADDING_BOTTOM, "PRODUCTS", ListProducts.class)
    );
    tabs.setOrientation(Tabs.Orientation.VERTICAL);
    tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
    return tabs;
  }

  public Tab createTab(VaadinIcon icon, String name, Class view) {
    // Icon img = icon.create();
    // img.getStyle().setHeight("20px");
    RouterLink link = new RouterLink(view);
    //<theme-editor-local-classname>
    link.addClassName("main-layout-a-1");

    link.add(new Span(name));
    // link.add(img, new Span(name));
    link.setTabIndex(-1);
    return new Tab(link);
  }
  
  //////////////////////////

}
