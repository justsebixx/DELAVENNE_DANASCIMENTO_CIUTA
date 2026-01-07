import React, { useState, useEffect } from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  PointElement,
  LineElement,
  RadialLinearScale,
  Filler,
} from 'chart.js';
import { Bar, Pie, Line, Doughnut, Radar, PolarArea } from 'react-chartjs-2';
import './Statistics.css';

// Enregistrer les composants Chart.js
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  PointElement,
  LineElement,
  RadialLinearScale,
  Filler
);

const Statistics = () => {
  // Données pour le dashboard
  const [stats, setStats] = useState({
    totalLivres: 0,
    livresDisponibles: 0,
    livresEmpruntes: 0,
    totalUtilisateurs: 0,
    empruntsEnCours: 0,
    empruntsEnRetard: 0,
  });

  const [topLivres, setTopLivres] = useState([]);
  const [loading, setLoading] = useState(true);

  // Charger les statistiques au montage du composant
  useEffect(() => {
    fetchDashboardStats();
  }, []);

  const fetchDashboardStats = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/dashboard/stats');
      if (response.ok) {
        const data = await response.json();
        setStats({
          totalLivres: data.totalLivres,
          livresDisponibles: data.livresDisponibles,
          livresEmpruntes: data.livresEmpruntes,
          totalUtilisateurs: data.totalUtilisateurs,
          empruntsEnCours: data.empruntsEnCours,
          empruntsEnRetard: data.empruntsEnRetard,
        });
        setTopLivres(data.topLivres || []);
      }
    } catch (error) {
      console.error('Erreur lors du chargement des statistiques:', error);
    } finally {
      setLoading(false);
    }
  };

  // Calcul du taux de retard
  const tauxRetard = stats.empruntsEnCours > 0 
    ? ((stats.empruntsEnRetard / stats.empruntsEnCours) * 100).toFixed(1) 
    : 0;

  // Calcul du taux d'utilisation
  const tauxUtilisation = stats.totalLivres > 0 
    ? ((stats.livresEmpruntes / (stats.livresDisponibles + stats.livresEmpruntes)) * 100).toFixed(1) 
    : 0;

  // Données pour le graphique en barres - Emprunts par mois
  const barChartData = {
    labels: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin', 'Juil', 'Août', 'Sep', 'Oct', 'Nov', 'Déc'],
    datasets: [
      {
        label: 'Emprunts 2025',
        data: [65, 59, 80, 81, 56, 55, 40, 35, 70, 85, 92, 78],
        backgroundColor: 'rgba(54, 162, 235, 0.7)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1,
      },
      {
        label: 'Emprunts 2024',
        data: [45, 49, 60, 71, 46, 45, 30, 25, 55, 65, 72, 58],
        backgroundColor: 'rgba(255, 99, 132, 0.7)',
        borderColor: 'rgba(255, 99, 132, 1)',
        borderWidth: 1,
      },
    ],
  };

  // Données pour le graphique en camembert - Répartition par catégorie
  const pieChartData = {
    labels: ['Roman', 'Science-Fiction', 'Histoire', 'Science', 'Informatique', 'Autre'],
    datasets: [
      {
        data: [35, 20, 15, 12, 10, 8],
        backgroundColor: [
          '#FF6384',
          '#36A2EB',
          '#FFCE56',
          '#4BC0C0',
          '#9966FF',
          '#FF9F40',
        ],
        hoverBackgroundColor: [
          '#FF6384',
          '#36A2EB',
          '#FFCE56',
          '#4BC0C0',
          '#9966FF',
          '#FF9F40',
        ],
      },
    ],
  };

  // Données pour le graphique en ligne - Évolution des inscriptions
  const lineChartData = {
    labels: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin', 'Juil', 'Août', 'Sep', 'Oct', 'Nov', 'Déc'],
    datasets: [
      {
        label: 'Nouveaux utilisateurs',
        data: [12, 19, 15, 25, 22, 30, 18, 15, 28, 35, 40, 32],
        fill: false,
        borderColor: '#4BC0C0',
        backgroundColor: '#4BC0C0',
        tension: 0.4,
      },
    ],
  };

  // Données pour le graphique Doughnut - État des livres
  const doughnutChartData = {
    labels: ['Disponibles', 'Empruntés'],
    datasets: [
      {
        data: [stats.livresDisponibles, stats.livresEmpruntes],
        backgroundColor: ['#4BC0C0', '#FF6384'],
        hoverBackgroundColor: ['#4BC0C0', '#FF6384'],
      },
    ],
  };

  // Données pour le graphique Radar - Métriques de performance
  const radarChartData = {
    labels: ['Satisfaction', 'Disponibilité', 'Diversité', 'Fréquentation', 'Retours à temps', 'Nouveautés'],
    datasets: [
      {
        label: 'Performance 2025',
        data: [85, 78, 92, 88, 100 - parseFloat(tauxRetard), 82],
        backgroundColor: 'rgba(54, 162, 235, 0.2)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 2,
        pointBackgroundColor: 'rgba(54, 162, 235, 1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(54, 162, 235, 1)',
      },
    ],
  };

  // Données pour le graphique PolarArea - Top 5 livres empruntés
  const polarChartData = {
    labels: topLivres.slice(0, 5).map(livre => livre.titre || 'N/A'),
    datasets: [
      {
        data: topLivres.slice(0, 5).map(livre => livre.nbEmprunts || 0),
        backgroundColor: [
          'rgba(255, 99, 132, 0.7)',
          'rgba(54, 162, 235, 0.7)',
          'rgba(255, 206, 86, 0.7)',
          'rgba(75, 192, 192, 0.7)',
          'rgba(153, 102, 255, 0.7)',
        ],
        borderWidth: 1,
      },
    ],
  };

  // Options communes pour les graphiques
  const barOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: 'Emprunts mensuels',
        font: { size: 16 },
      },
    },
  };

  const pieOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'right',
      },
      title: {
        display: true,
        text: 'Répartition par catégorie',
        font: { size: 16 },
      },
    },
  };

  const lineOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: 'Évolution des inscriptions',
        font: { size: 16 },
      },
    },
  };

  const doughnutOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'right',
      },
      title: {
        display: true,
        text: 'État des livres',
        font: { size: 16 },
      },
    },
  };

  const radarOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: 'Métriques de performance (%)',
        font: { size: 16 },
      },
    },
    scales: {
      r: {
        beginAtZero: true,
        max: 100,
      },
    },
  };

  const polarOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'right',
      },
      title: {
        display: true,
        text: 'Top 5 livres les plus empruntés',
        font: { size: 16 },
      },
    },
  };

  if (loading) {
    return (
      <div className="statistics-container">
        <div className="loading">Chargement des statistiques...</div>
      </div>
    );
  }

  return (
    <div className="statistics-container">
      <h1 className="statistics-title">📊 Statistiques de la Bibliothèque</h1>

      {/* Cartes statistiques principales */}
      <div className="stats-cards">
        <div className="stat-card primary">
          <div className="stat-icon">📚</div>
          <div className="stat-info">
            <h3>{stats.totalLivres}</h3>
            <p>Total Livres</p>
          </div>
        </div>

        <div className="stat-card success">
          <div className="stat-icon">✅</div>
          <div className="stat-info">
            <h3>{stats.livresDisponibles}</h3>
            <p>Disponibles</p>
          </div>
        </div>

        <div className="stat-card warning">
          <div className="stat-icon">📖</div>
          <div className="stat-info">
            <h3>{stats.livresEmpruntes}</h3>
            <p>Empruntés</p>
          </div>
        </div>

        <div className="stat-card info">
          <div className="stat-icon">👥</div>
          <div className="stat-info">
            <h3>{stats.totalUtilisateurs}</h3>
            <p>Utilisateurs</p>
          </div>
        </div>

        <div className="stat-card purple">
          <div className="stat-icon">🔄</div>
          <div className="stat-info">
            <h3>{stats.empruntsEnCours}</h3>
            <p>Emprunts en cours</p>
          </div>
        </div>

        <div className="stat-card danger">
          <div className="stat-icon">⚠️</div>
          <div className="stat-info">
            <h3>{stats.empruntsEnRetard}</h3>
            <p>En retard</p>
          </div>
        </div>
      </div>

      {/* Section indicateurs clés */}
      <section className="key-indicators">
        <h2>📈 Indicateurs Clés</h2>
        <div className="indicators-grid">
          <div className="indicator-card">
            <div className="indicator-value">{tauxRetard}%</div>
            <div className="indicator-label">Taux de retard</div>
            <div className="indicator-bar">
              <div 
                className="indicator-fill danger" 
                style={{ width: `${Math.min(tauxRetard, 100)}%` }}
              ></div>
            </div>
          </div>

          <div className="indicator-card">
            <div className="indicator-value">{tauxUtilisation}%</div>
            <div className="indicator-label">Taux d'utilisation</div>
            <div className="indicator-bar">
              <div 
                className="indicator-fill success" 
                style={{ width: `${Math.min(tauxUtilisation, 100)}%` }}
              ></div>
            </div>
          </div>

          <div className="indicator-card">
            <div className="indicator-value">{stats.empruntsEnCours}</div>
            <div className="indicator-label">Emprunts actifs</div>
            <div className="indicator-bar">
              <div 
                className="indicator-fill primary" 
                style={{ width: '100%' }}
              ></div>
            </div>
          </div>
        </div>
      </section>

      {/* Top 5 livres les plus empruntés */}
      <section className="top-books-section">
        <h2>🏆 Livres les plus populaires</h2>
        <div className="top-books-list">
          {topLivres.slice(0, 5).map((livre, index) => (
            <div key={index} className="top-book-item">
              <div className="rank">#{index + 1}</div>
              <div className="book-info">
                <div className="book-title">{livre.titre}</div>
                <div className="book-author">{livre.auteur}</div>
              </div>
              <div className="borrow-count">{livre.nbEmprunts} emprunts</div>
            </div>
          ))}
          {topLivres.length === 0 && (
            <p className="no-data">Aucune donnée disponible</p>
          )}
        </div>
      </section>

      {/* Graphiques */}
      <section className="charts-section">
        <h2>📉 Visualisations</h2>
        <div className="charts-grid">
          <div className="chart-container">
            <Bar data={barChartData} options={barOptions} />
          </div>

          <div className="chart-container">
            <Pie data={pieChartData} options={pieOptions} />
          </div>

          <div className="chart-container">
            <Line data={lineChartData} options={lineOptions} />
          </div>

          <div className="chart-container">
            <Doughnut data={doughnutChartData} options={doughnutOptions} />
          </div>

          <div className="chart-container">
            <Radar data={radarChartData} options={radarOptions} />
          </div>

          <div className="chart-container">
            <PolarArea data={polarChartData} options={polarOptions} />
          </div>
        </div>
      </section>
    </div>
  );
};

export default Statistics;
