package com.denisyan.socks_must_flow.views;

import com.denisyan.socks_must_flow.entity.Sock;
import com.denisyan.socks_must_flow.service.SocksService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Map;

//для работы всего класса нужна лицензия Vaadin :(

@Route("/dash")
@PageTitle("Dashboard | Socks")
public class DashBoardView extends VerticalLayout {

    private final SocksService socksService;

    public DashBoardView(SocksService socksService) {
        this.socksService = socksService;
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(
                getSocksStats(),
                getColorStats()
        );
    }

    private Component getSocksStats() {
        return new Span(socksService.findAll().stream().map(Sock::getQuantity).count() + " socks");
    }


    private Component getColorStats() {
        Chart chart = new Chart(ChartType.PIE);

        DataSeries dataSeries = new DataSeries();

        Map<String, Integer> allColorsAndQuantity = socksService.getAllColorsAndQuantity();
        allColorsAndQuantity.forEach((k, v) ->
                dataSeries.add(new DataSeriesItem(k, v)));

        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }
}
