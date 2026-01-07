import React, { useState, useEffect } from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend,
  ArcElement,
} from 'chart.js';
import { Doughnut, PolarArea } from 'react-chartjs-2';
import './Statistics.css';
import { api } from '../services/apiService';

// Enregistrement des composants Chart.js...
ChartJS.register(
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend,
  ArcElement
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
      const data = await api.get('/dashboard/stats');
      setStats({
        totalLivres: data.totalLivres,
        livresDisponibles: data.livresDisponibles,
        livresEmpruntes: data.livresEmpruntes,
        totalUtilisateurs: data.totalUtilisateurs,
        empruntsEnCours: data.empruntsEnCours,
        empruntsEnRetard: data.empruntsEnRetard,
      });
      setTopLivres(data.topLivres || []);
    } catch (error) {
      console.error('Erreur lors du chargement des statistiques:', error);
    } finally {
      setLoading(false);
    }
  };

  const tauxRetard = stats.empruntsEnCours > 0 
    ? ((stats.empruntsEnRetard / stats.empruntsEnCours) * 100).toFixed(1) 
    : 0;

  const tauxUtilisation = stats.totalLivres > 0 
    ? ((stats.livresEmpruntes / (stats.livresDisponibles + stats.livresEmpruntes)) * 100).toFixed(1) 
    : 0;

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
            <Doughnut data={doughnutChartData} options={doughnutOptions} />
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
