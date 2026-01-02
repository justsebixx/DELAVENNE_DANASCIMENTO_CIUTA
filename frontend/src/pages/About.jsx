function About() {
    return (
        <div className="page-container">
            <h1>À propos</h1>

            <section className="about-section">
                <h2>Notre Bibliothèque</h2>
                <p>
                    La Bibliothèque Universitaire d'Amiens est un établissement dédié à la diffusion
                    du savoir et à l'accompagnement des étudiants, chercheurs et passionnés de lecture.
                </p>
                <p>
                    Fondée pour servir la communauté universitaire, notre bibliothèque offre un accès
                    à des milliers d'ouvrages, revues scientifiques et ressources numériques.
                </p>
            </section>

            <section className="about-section">
                <h2>Notre Mission</h2>
                <ul>
                    <li>Faciliter l'accès à la connaissance pour tous</li>
                    <li>Accompagner les étudiants dans leur parcours académique</li>
                    <li>Préserver et valoriser le patrimoine documentaire</li>
                    <li>Offrir un espace de travail et de recherche adapté</li>
                </ul>
            </section>

            <section className="about-section">
                <h2>Nos Services</h2>
                <ul>
                    <li>Prêt de livres et documents</li>
                    <li>Accès aux ressources numériques</li>
                    <li>Espaces de travail individuel et en groupe</li>
                    <li>Aide à la recherche documentaire</li>
                    <li>Formations aux outils de recherche</li>
                </ul>
            </section>
        </div>
    );
}

export default About;
