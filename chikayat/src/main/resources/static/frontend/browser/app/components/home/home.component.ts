import { Component, OnInit } from '@angular/core';
import { ReclamationService } from '../../services/reclamation.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
declare var bootstrap: any; // Pour utiliser Bootstrap si vous l’incluez via script
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Chart, PieController, ArcElement, Tooltip, Legend } from 'chart.js';
import ChartDataLabels from 'chartjs-plugin-datalabels';
import { environment } from '../../../environments/environment';
Chart.register(PieController, ArcElement, Tooltip, Legend, ChartDataLabels);

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  private baseUrl = environment.apiUrl;
  
  private apiUrl = `${this.baseUrl}/reclamations`;

  stats: any = {};
  durationType: string = 'jours';
  durationValue: number = 7;
  currentTime: string = '';

  // Propriété pour stocker la liste des réclamations filtrées
  reclamations: any[] = [];

  constructor(private reclamationService: ReclamationService, private http: HttpClient) {}

  ngOnInit(): void {
    this.loadStats();
    this.startTimer();
    this.loadCharts();

  }
    private getAuthHeaders(): HttpHeaders {
      const token = localStorage.getItem('token');
      return new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    }
  loadCharts() {
    const headers = this.getAuthHeaders();
    // Pour les 7 derniers jours
    this.http.get<any>(`${this.apiUrl}/stats/duration/7days`, { headers })
      .subscribe((stats7: any) => {
        console.log('Stats 7 jours:', stats7);
        this.createChart('chart7days', stats7, '7 Derniers Jours');
      });
    // Pour les 30 derniers jours
    this.http.get<any>(`${this.apiUrl}/stats/duration/30days`, { headers })
      .subscribe((stats30: any) => {
        console.log('Stats 30 jours:', stats30);
        this.createChart('chart30days', stats30, '30 Derniers Jours');
      });
    // Pour l'année في انتظار الإرسال إلى السلطات
    this.http.get<any>(`${this.apiUrl}/stats/duration/year`, { headers })
      .subscribe((statsYear: any) => {
        console.log('Stats Année:', statsYear);
        this.createChart('chartYear', statsYear, "Année في انتظار الإرسال إلى السلطات");
      });
  }

  createChart(canvasId: string, stats: any, title: string) {
    const canvas = document.getElementById(canvasId) as HTMLCanvasElement;
    if (!canvas) {
      console.error(`Canvas with id ${canvasId} not found`);
      return;
    }
    const ctx = canvas.getContext('2d');
    if (!ctx) {
      console.error(`Unable to get 2D context for canvas with id ${canvasId}`);
      return;
    }
    const data = {
      labels: ['Total', 'En Cours', 'Envoyées Autorité', 'Répondu Autorité', 'Clôturées'],
      datasets: [{
        data: [stats.total, stats.enCours, stats.envoyeesAutorite, stats.reponduAutorite, stats.cloturees],
        backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF']
      }]
    };
    new Chart(ctx, {
      type: 'pie',
      data: data,
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: title
          },
          datalabels: {
            color: '#fff',
            anchor: 'center', // place le label au centre du segment
            align: 'center',  // aligne le texte au centre
            formatter: (value: number) => {
              return value === 0 ? '' : value; // masque le 0
            },
            font: {
              weight: 'bold'
            }
          }
        }
      }
    });
  }
    
  startTimer() {
    this.updateTime();
    setInterval(() => this.updateTime(), 1000);
  }

  updateTime() {
    const now = new Date();
    // Affiche les chiffres en style latin grâce à numberingSystem: 'latn'
    this.currentTime = now.toLocaleString('ar-EG', { numberingSystem: 'latn' });
  }

  loadStats() {
    this.reclamationService.getStats(this.durationType, this.durationValue)
      .subscribe(data => this.stats = data);
  }

  // Lorsque l'utilisateur clique sur une statistique, charge les réclamations correspondantes et ouvre le modal
  openReclamationsList(flag: string) {
    this.reclamationService.getReclamationsByFlag(flag)
      .subscribe((data: any[]) => {
        this.reclamations = data;
        this.showListModal();
      });
  }

  showListModal() {
    const modalEl = document.getElementById('reclamationsListModal');
    if (modalEl) {
      const modalInstance = new bootstrap.Modal(modalEl);
      modalInstance.show();
    }
  }

  closeListModal() {
    const modalEl = document.getElementById('reclamationsListModal');
    if (modalEl) {
      const modalInstance = bootstrap.Modal.getInstance(modalEl);
      modalInstance.hide();
    }
  }
}
