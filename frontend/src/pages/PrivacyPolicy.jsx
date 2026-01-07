function PrivacyPolicy() {
    return (
        <div className="page-container">
            <h1>Politique de Confidentialité</h1>

            <section className="privacy-section">
                <h2>Introduction</h2>
                <p>
                    La Bibliothèque Universitaire d'Amiens s'engage à protéger la vie privée
                    des utilisateurs de son site web et de ses services. Cette politique de
                    confidentialité explique comment nous collectons, utilisons et protégeons
                    vos données personnelles.
                </p>
            </section>

            <section className="privacy-section">
                <h2>Données collectées</h2>
                <p>Nous pouvons collecter les informations suivantes :</p>
                <ul>
                    <li>Nom et prénom</li>
                    <li>Adresse email</li>
                    <li>Numéro d'étudiant ou de membre</li>
                    <li>Historique des emprunts</li>
                    <li>Données de connexion (logs)</li>
                </ul>
            </section>

            <section className="privacy-section">
                <h2>Utilisation des données</h2>
                <p>Vos données sont utilisées pour :</p>
                <ul>
                    <li>Gérer votre compte utilisateur</li>
                    <li>Traiter vos emprunts et réservations</li>
                    <li>Vous informer des échéances de retour</li>
                    <li>Améliorer nos services</li>
                    <li>Répondre à vos demandes de contact</li>
                </ul>
            </section>

            <section className="privacy-section">
                <h2>Protection des données</h2>
                <p>
                    Nous mettons en œuvre des mesures de sécurité appropriées pour protéger
                    vos données contre tout accès non autorisé, modification, divulgation ou
                    destruction.
                </p>
            </section>

            <section className="privacy-section">
                <h2>Vos droits</h2>
                <p>Conformément au RGPD, vous disposez des droits suivants :</p>
                <ul>
                    <li>Droit d'accès à vos données</li>
                    <li>Droit de rectification</li>
                    <li>Droit à l'effacement</li>
                    <li>Droit à la portabilité</li>
                    <li>Droit d'opposition</li>
                </ul>
            </section>

            <section className="privacy-section">
                <h2>Contact</h2>
                <p>
                    Pour toute question concernant cette politique de confidentialité ou pour
                    exercer vos droits, contactez-nous à : bibliotheque@univ-amiens.fr
                </p>
            </section>
        </div>
    );
}

export default PrivacyPolicy;
