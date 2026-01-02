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
import './Admin.css';

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

const Admin = () => {
  // État pour le formulaire d'ajout de livre
  const [formData, setFormData] = useState({
    titre: '',
    auteur: '',
    isbn: '',
    categorie: '',
    nombreExemplaires: 1,
    anneePublication: '',
    editeur: '',
    description: '',
  });

  const [message, setMessage] = useState({ type: '', text: '' });
  const [livres, setLivres] = useState([]);

  // Données simulées pour le dashboard
  const [stats, setStats] = useState({
    totalLivres: 156,
    livresDisponibles: 124,
    livresEmpruntes: 32,
    totalUtilisateurs: 89,
    empruntsEnCours: 45,
    empruntsEnRetard: 7,
  });

  // Catégories pour le formulaire
  const categories = [
    'Roman',
    'Science-Fiction',
    'Fantasy',
    'Histoire',
    'Science',
    'Informatique',
    'Philosophie',
    'Art',
    'Biographie',
    'Jeunesse',
    'BD/Manga',
    'Autre',
  ];

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
    labels: ['Disponibles', 'Empruntés', 'En réparation', 'Perdus'],
    datasets: [
      {
        data: [124, 32, 5, 3],
        backgroundColor: ['#4BC0C0', '#FF6384', '#FFCE56', '#9966FF'],
        hoverBackgroundColor: ['#4BC0C0', '#FF6384', '#FFCE56', '#9966FF'],
      },
    ],
  };

  // Données pour le graphique Radar - Métriques de performance
  const radarChartData = {
    labels: ['Satisfaction', 'Disponibilité', 'Diversité', 'Fréquentation', 'Retours à temps', 'Nouveautés'],
    datasets: [
      {
        label: 'Performance 2025',
        data: [85, 78, 92, 88, 75, 82],
        backgroundColor: 'rgba(54, 162, 235, 0.2)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 2,
        pointBackgroundColor: 'rgba(54, 162, 235, 1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(54, 162, 235, 1)',
      },
      {
        label: 'Performance 2024',
        data: [75, 70, 85, 80, 68, 72],
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
        borderColor: 'rgba(255, 99, 132, 1)',
        borderWidth: 2,
        pointBackgroundColor: 'rgba(255, 99, 132, 1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(255, 99, 132, 1)',
      },
    ],
  };

  // Données pour le graphique PolarArea - Top 5 livres empruntés
  const polarChartData = {
    labels: ['1984 (G. Orwell)', 'Le Petit Prince', 'Harry Potter', 'Sapiens', 'Clean Code'],
    datasets: [
      {
        data: [45, 38, 35, 28, 22],
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

  // Gestion du formulaire
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Validation basique
    if (!formData.titre || !formData.auteur || !formData.isbn || !formData.categorie) {
      setMessage({ type: 'error', text: 'Veuillez remplir tous les champs obligatoires.' });
      return;
    }

    // Simuler l'ajout du livre
    const nouveauLivre = {
      id: Date.now(),
      ...formData,
      dateAjout: new Date().toLocaleDateString('fr-FR'),
    };

    setLivres((prev) => [nouveauLivre, ...prev]);
    setMessage({ type: 'success', text: `Le livre "${formData.titre}" a été ajouté avec succès !` });

    // Réinitialiser le formulaire
    setFormData({
      titre: '',
      auteur: '',
      isbn: '',
      categorie: '',
      nombreExemplaires: 1,
      anneePublication: '',
      editeur: '',
      description: '',
    });

    // Effacer le message après 3 secondes
    setTimeout(() => setMessage({ type: '', text: '' }), 3000);
  };

  return (
    <div className="admin-container">
      <h1 className="admin-title">🔐 Panneau d'Administration</h1>

      {/* Section Dashboard */}
      <section className="dashboard-section">
        <h2>📊 Dashboard</h2>

        {/* Cartes statistiques */}
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

        {/* Graphiques */}
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

export default Admin;
