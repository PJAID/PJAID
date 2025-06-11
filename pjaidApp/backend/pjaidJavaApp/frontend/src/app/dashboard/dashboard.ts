import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Chart, ChartConfiguration, ChartType, ChartData, ActiveElement } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';

import {
  BarController,
  BarElement,
  CategoryScale,
  LinearScale,
  ArcElement,
  Tooltip,
  Legend
} from 'chart.js';
import {DatePipe, NgForOf, NgIf} from '@angular/common';


Chart.register(
  BarController,
  BarElement,
  CategoryScale,
  LinearScale,
  ArcElement,
  Tooltip,
  Legend
);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NgChartsModule, NgIf, NgForOf, DatePipe],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent implements OnInit {
  statusCounts: any = {};
  ticketChart: ChartConfiguration<'bar'>['data'] = { labels: [], datasets: [] };
  selectedStatus: string | null = null;
  tickets: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchStatusData();
  }

  fetchStatusData(): void {
    this.http.get<any>('http://localhost:8080/ticket/status-summary')
      .subscribe(data => {
        this.statusCounts = data;
        this.ticketChart = {
          labels: Object.keys(data),
          datasets: [{
            label: 'Zgłoszenia',
            data: Object.values(data),
            backgroundColor: ['#4dc9f6', '#f67019', '#f53794', '#537bc4', '#9966ff']
          }]
        };
      });
  }

  onChartClick(event: any, active: {}[] | undefined): void {
    if (active && active.length > 0) {
      const chartElement = active[0] as ActiveElement;
      const index = chartElement.index;
      const status = this.ticketChart.labels?.[index] as string;

      this.selectedStatus = status;
      this.fetchTicketsByStatus(status);
    }
  }

  fetchTicketsByStatus(status: string): void {
    this.http.get<any[]>(`http://localhost:8080/ticket?status=${status}`)
      .subscribe(data => {
        this.tickets = data;
      });
  }
}

export class Dashboard {
}
