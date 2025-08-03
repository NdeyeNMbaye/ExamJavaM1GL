document.addEventListener('DOMContentLoaded', () => {
    // Sélection des éléments du DOM
    const sectorsTableBody = document.getElementById('sectors-table-body');
    const addSectorBtn = document.getElementById('add-sector-btn');
    const sectorFormContainer = document.getElementById('sector-form-container');
    const sectorForm = document.getElementById('sector-form');
    const sectorIdInput = document.getElementById('sector-id');
    const sectorNameInput = document.getElementById('sector-name');
    const cancelBtn = document.getElementById('cancel-btn');
    const formTitle = document.getElementById('form-title');
    const overlay = document.getElementById('overlay');

    // Éléments pour le dialogue de confirmation
    const confirmModal = document.createElement('div');
    confirmModal.className = 'fixed inset-0 hidden items-center justify-center bg-black bg-opacity-50 z-[1001]';
    confirmModal.innerHTML = `
        <div class="bg-white p-6 rounded-lg shadow-xl w-80">
            <p class="text-gray-800 text-lg mb-4">Êtes-vous sûr de vouloir supprimer ce secteur ?</p>
            <div class="flex justify-end space-x-2">
                <button id="cancel-delete-btn" class="bg-gray-300 text-gray-800 px-4 py-2 rounded-lg hover:bg-gray-400">Annuler</button>
                <button id="confirm-delete-btn" class="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700">Supprimer</button>
            </div>
        </div>
    `;
    document.body.appendChild(confirmModal);

    const confirmDeleteBtn = document.getElementById('confirm-delete-btn');
    const cancelDeleteBtn = document.getElementById('cancel-delete-btn');

    // URL de base de votre API REST
    const API_URL = '/api/sectors';

    // Fonction pour afficher le formulaire (mode ajout ou modification)
    const showForm = () => {
        sectorFormContainer.style.display = 'block';
        overlay.classList.remove('hidden');
    };

    // Fonction pour masquer le formulaire
    const hideForm = () => {
        sectorFormContainer.style.display = 'none';
        overlay.classList.add('hidden');
        sectorForm.reset();
        sectorIdInput.value = '';
        formTitle.textContent = "Ajouter un secteur";
    };

    // Fonction pour afficher les secteurs dans le tableau
    const renderSectors = async () => {
        sectorsTableBody.innerHTML = '';
        try {
            const response = await fetch(API_URL);
            if (!response.ok) {
                throw new Error('Erreur lors de la récupération des secteurs');
            }
            const sectors = await response.json();
            sectors.forEach(sector => {
                const row = document.createElement('tr');
                row.className = 'hover:bg-gray-100 transition-colors duration-200';
                row.innerHTML = `
                    <td class="px-6 py-4 whitespace-nowrap">${sector.name}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                        <button onclick="editSector(${sector.id})" class="text-blue-600 hover:text-blue-900 mx-2">
                            <i class="fas fa-pencil-alt"></i>
                        </button>
                        <button onclick="deleteSector(${sector.id})" class="text-red-600 hover:text-red-900 mx-2">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </td>
                `;
                sectorsTableBody.appendChild(row);
            });
        } catch (error) {
            console.error("Erreur lors du chargement des secteurs:", error);
            // Afficher un message d'erreur à l'utilisateur
            sectorsTableBody.innerHTML = `<tr><td colspan="2" class="text-center text-red-500 py-4">Erreur lors du chargement des données.</td></tr>`;
        }
    };

    // Gérer l'ajout/modification via le formulaire
    const saveOrUpdateSector = async (sectorDto) => {
        const isUpdate = sectorDto.id;
        const method = isUpdate ? 'PUT' : 'POST';
        const url = isUpdate ? `${API_URL}/${sectorDto.id}` : API_URL;

        try {
            const response = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(sectorDto)
            });

            if (!response.ok) {
                throw new Error(`Erreur lors de l'opération: ${response.statusText}`);
            }

            await renderSectors(); // Recharger la liste après l'opération
            hideForm();
        } catch (error) {
            console.error("Erreur lors de la sauvegarde du secteur:", error);
            // Afficher un message d'erreur
            alert("Une erreur est survenue lors de la sauvegarde du secteur.");
        }
    };

    // Gérer la soumission du formulaire
    sectorForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const id = sectorIdInput.value ? parseInt(sectorIdInput.value) : null;
        const name = sectorNameInput.value.trim();

        if (name) {
            const sectorDto = { id: id, name: name };
            saveOrUpdateSector(sectorDto);
        }
    });

    // Gérer l'ajout d'un nouveau secteur
    addSectorBtn.addEventListener('click', () => {
        formTitle.textContent = "Ajouter un secteur";
        hideForm(); // S'assurer que le formulaire est propre avant de le montrer
        showForm();
    });

    // Gérer la modification d'un secteur (fonction globale pour les boutons)
    window.editSector = async (id) => {
        try {
            const response = await fetch(`${API_URL}/${id}`);
            if (!response.ok) {
                throw new Error('Secteur non trouvé');
            }
            const sectorToEdit = await response.json();
            formTitle.textContent = "Modifier un secteur";
            sectorIdInput.value = sectorToEdit.id;
            sectorNameInput.value = sectorToEdit.name;
            showForm();
        } catch (error) {
            console.error("Erreur lors de la récupération du secteur à modifier:", error);
            alert("Secteur non trouvé ou erreur de chargement.");
        }
    };

    // Gérer la suppression d'un secteur (fonction globale pour les boutons)
    window.deleteSector = (id) => {
        confirmModal.classList.remove('hidden');
        confirmModal.classList.add('flex');

        confirmDeleteBtn.onclick = async () => {
            try {
                const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
                if (!response.ok) {
                    throw new Error('Erreur lors de la suppression');
                }
                await renderSectors();
                confirmModal.classList.add('hidden');
                confirmModal.classList.remove('flex');
            } catch (error) {
                console.error("Erreur lors de la suppression:", error);
                alert("Une erreur est survenue lors de la suppression du secteur.");
                confirmModal.classList.add('hidden');
                confirmModal.classList.remove('flex');
            }
        };

        cancelDeleteBtn.onclick = () => {
            confirmModal.classList.add('hidden');
            confirmModal.classList.remove('flex');
        };
    };

    // Gérer l'annulation du formulaire
    cancelBtn.addEventListener('click', hideForm);
    overlay.addEventListener('click', hideForm);

    // Rendre les secteurs au chargement de la page
    renderSectors();
});
