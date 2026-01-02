function LegalNotice() {
    return (
        <div className="page-container">
            <h1>Mentions Légales</h1>

            <section className="legal-section">
                <h2>Éditeur du site</h2>
                <p>
                    Bibliothèque Universitaire d'Amiens<br />
                    Campus Universitaire<br />
                    80000 Amiens, France
                </p>
            </section>

            <section className="legal-section">
                <h2>Directeur de la publication</h2>
                <p>Le Directeur de la Bibliothèque Universitaire</p>
            </section>

            <section className="legal-section">
                <h2>Hébergement</h2>
                <p>
                    Ce site est hébergé par l'Université d'Amiens.<br />
                    Adresse : Campus Universitaire, 80000 Amiens
                </p>
            </section>

            <section className="legal-section">
                <h2>Propriété intellectuelle</h2>
                <p>
                    L'ensemble du contenu de ce site (textes, images, vidéos) est protégé
                    par le droit d'auteur. Toute reproduction, même partielle, est interdite
                    sans autorisation préalable.
                </p>
            </section>

            <section className="legal-section">
                <h2>Données personnelles</h2>
                <p>
                    Conformément au Règlement Général sur la Protection des Données (RGPD),
                    vous disposez d'un droit d'accès, de rectification et de suppression des
                    données vous concernant. Pour exercer ce droit, veuillez nous contacter
                    à l'adresse : bibliotheque@univ-amiens.fr
                </p>
            </section>

            <section className="legal-section">
                <h2>Cookies</h2>
                <p>
                    Ce site utilise des cookies pour améliorer l'expérience utilisateur.
                    En poursuivant votre navigation, vous acceptez l'utilisation de ces cookies.
                </p>
            </section>
        </div>
    );
}

export default LegalNotice;
