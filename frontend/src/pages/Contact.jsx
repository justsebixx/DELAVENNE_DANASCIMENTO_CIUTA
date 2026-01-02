import { useState } from 'react';

function Contact() {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        subject: '',
        message: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // TODO: Implémenter l'envoi du formulaire
        alert('Message envoyé ! Nous vous répondrons dans les plus brefs délais.');
        setFormData({ name: '', email: '', subject: '', message: '' });
    };

    return (
        <div className="page-container">
            <h1>Contact</h1>

            <div className="contact-content">
                <section className="contact-info">
                    <h2>Nos Coordonnées</h2>
                    <div className="info-item">
                        <strong>Adresse :</strong>
                        <p>Campus Universitaire<br />80000 Amiens, France</p>
                    </div>
                    <div className="info-item">
                        <strong>Téléphone :</strong>
                        <p>03 22 XX XX XX</p>
                    </div>
                    <div className="info-item">
                        <strong>Email :</strong>
                        <p>bibliotheque@univ-amiens.fr</p>
                    </div>
                    <div className="info-item">
                        <strong>Horaires d'ouverture :</strong>
                        <p>Lundi - Vendredi : 8h30 - 19h00<br />Samedi : 9h00 - 17h00</p>
                    </div>
                </section>

                <section className="contact-form-section">
                    <h2>Nous écrire</h2>
                    <form onSubmit={handleSubmit} className="contact-form">
                        <div className="form-group">
                            <label htmlFor="name">Nom complet</label>
                            <input
                                type="text"
                                id="name"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="email">Email</label>
                            <input
                                type="email"
                                id="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="subject">Sujet</label>
                            <input
                                type="text"
                                id="subject"
                                name="subject"
                                value={formData.subject}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="message">Message</label>
                            <textarea
                                id="message"
                                name="message"
                                rows="5"
                                value={formData.message}
                                onChange={handleChange}
                                required
                            ></textarea>
                        </div>
                        <button type="submit" className="submit-btn">Envoyer</button>
                    </form>
                </section>
            </div>
        </div>
    );
}

export default Contact;
