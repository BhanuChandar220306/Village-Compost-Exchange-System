// ==========================================
// FARMER DASHBOARD
// ==========================================

document.addEventListener("DOMContentLoaded", () => {

    // ------------------------------------------
    // Authentication check
    // ------------------------------------------

    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    if (!token || role !== "FARMER") {
        window.location.href = "index.html";
        return;
    }


    // ------------------------------------------
    // Display farmer name
    // ------------------------------------------

    const farmerName = localStorage.getItem("fullName");

    if (farmerName) {
        document.getElementById("farmerName").textContent = farmerName;
    }


    // ------------------------------------------
    // Event listeners
    // ------------------------------------------

    document
        .getElementById("compostRequestForm")
        .addEventListener("submit", submitCompostRequest);


    document
        .getElementById("refreshRequestsBtn")
        .addEventListener("click", loadCompostRequests);


    document
        .getElementById("refreshAllocationsBtn")
        .addEventListener("click", loadAllocations);


    document
        .getElementById("logoutBtn")
        .addEventListener("click", logout);


    // ------------------------------------------
    // Initial loading
    // ------------------------------------------

    loadCompostRequests();

    loadAllocations();

});


// ==========================================
// SUBMIT COMPOST REQUEST
// ==========================================

async function submitCompostRequest(event) {

    event.preventDefault();

    const quantity =
        Number(document.getElementById("quantity").value);

    const requiredDate =
        document.getElementById("requiredDate").value;

    const message =
        document.getElementById("compostRequestMessage");


    if (!quantity || quantity <= 0) {

        message.textContent =
            "Please enter a valid compost quantity.";

        message.style.color = "#c62828";

        return;
    }


    if (!requiredDate) {

        message.textContent =
            "Please select a required date.";

        message.style.color = "#c62828";

        return;
    }


    message.textContent =
        "Submitting compost request...";

    message.style.color = "#555";


    try {

        await apiRequest("/api/compost/request", {

            method: "POST",

            body: JSON.stringify({
                quantity: quantity,
                requiredDate: requiredDate
            })

        });


        message.textContent =
            "Compost request submitted successfully.";

        message.style.color = "#2e7d32";


        // Reset form

        document
            .getElementById("compostRequestForm")
            .reset();


        // Reload requests

        loadCompostRequests();


    } catch (error) {

        console.error(
            "Compost request error:",
            error
        );

        message.textContent =
            error.message;

        message.style.color =
            "#c62828";
    }
}


// ==========================================
// LOAD MY COMPOST REQUESTS
// ==========================================

async function loadCompostRequests() {

    const tableBody =
        document.getElementById(
            "compostRequestsBody"
        );


    tableBody.innerHTML = `
        <tr>
            <td colspan="4" class="empty-message">
                Loading requests...
            </td>
        </tr>
    `;


    try {

        const response =
            await apiRequest("/api/compost/my");


        /*
         * Backend response:
         *
         * {
         *   success: true,
         *   message: "...",
         *   data: [...]
         * }
         */

        const requests =
            Array.isArray(response.data)
                ? response.data
                : [];


        updateRequestStats(requests);


        if (requests.length === 0) {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="4"
                        class="empty-message">
                        No compost requests found.
                    </td>
                </tr>
            `;

            return;
        }


        tableBody.innerHTML =
            requests.map(request => {

                return `
                    <tr>

                        <td>
                            #${request.id}
                        </td>

                        <td>
                            ${request.quantity}
                        </td>

                        <td>
                            ${formatDate(
                                request.requiredDate
                            )}
                        </td>

                        <td>
                            ${getStatusBadge(
                                request.status
                            )}
                        </td>

                    </tr>
                `;

            }).join("");


    } catch (error) {

        console.error(
            "Load compost requests error:",
            error
        );


        tableBody.innerHTML = `
            <tr>
                <td colspan="4"
                    class="empty-message">

                    Failed to load compost requests.

                </td>
            </tr>
        `;
    }
}


// ==========================================
// UPDATE REQUEST STATISTICS
// ==========================================

function updateRequestStats(requests) {

    const total =
        requests.length;


    const pending =
        requests.filter(
            request =>
                request.status === "PENDING"
        ).length;


    const approved =
        requests.filter(
            request =>
                request.status === "ACCEPTED"
        ).length;


    document.getElementById(
        "totalRequests"
    ).textContent = total;


    document.getElementById(
        "pendingRequests"
    ).textContent = pending;


    document.getElementById(
        "approvedRequests"
    ).textContent = approved;
}


// ==========================================
// LOAD MY ALLOCATIONS
// ==========================================

async function loadAllocations() {

    const tableBody =
        document.getElementById(
            "allocationsBody"
        );


    tableBody.innerHTML = `
        <tr>
            <td colspan="5"
                class="empty-message">

                Loading allocations...

            </td>
        </tr>
    `;


    try {

        const response =
            await apiRequest(
                "/api/allocation/my"
            );


        /*
         * Backend response:
         *
         * {
         *   success: true,
         *   message: "...",
         *   data: [...]
         * }
         */

        const allocations =
            Array.isArray(response.data)
                ? response.data
                : [];


        updateAllocationStats(
            allocations
        );


        if (allocations.length === 0) {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="5"
                        class="empty-message">

                        No compost allocations found.

                    </td>
                </tr>
            `;

            return;
        }


        tableBody.innerHTML =
            allocations.map(
                allocation => {

                    const completed =
                        allocation.status ===
                        "COMPLETED";


                    return `
                        <tr>

                            <td>
                                #${allocation.id}
                            </td>

                            <td>
                                ${allocation.allocatedQuantity}
                            </td>

                            <td>
                                ${formatDate(
                                    allocation.allocatedDate
                                )}
                            </td>

                            <td>
                                ${getStatusBadge(
                                    allocation.status
                                )}
                            </td>

                            <td>

                                ${
                                    completed

                                    ?

                                    `<span
                                        class="status-badge status-completed">
                                        Completed
                                    </span>`

                                    :

                                    `<button
                                        class="action-btn"
                                        onclick="completeAllocation(${allocation.id})">
                                        Mark Completed
                                    </button>`
                                }

                            </td>

                        </tr>
                    `;

                }
            ).join("");


    } catch (error) {

        console.error(
            "Load allocations error:",
            error
        );


        tableBody.innerHTML = `
            <tr>
                <td colspan="5"
                    class="empty-message">

                    Failed to load allocations.

                </td>
            </tr>
        `;
    }
}


// ==========================================
// UPDATE ALLOCATION STATISTICS
// ==========================================

function updateAllocationStats(
    allocations
) {

    const completed =
        allocations.filter(
            allocation =>
                allocation.status ===
                "COMPLETED"
        ).length;


    document.getElementById(
        "completedAllocations"
    ).textContent = completed;
}


// ==========================================
// COMPLETE ALLOCATION
// ==========================================

async function completeAllocation(id) {

    const confirmed =
        confirm(
            "Are you sure you want to mark this allocation as completed?"
        );


    if (!confirmed) {
        return;
    }


    try {

        await apiRequest(
            `/api/allocation/${id}/complete`,
            {
                method: "PUT"
            }
        );


        alert(
            "Allocation marked as completed."
        );


        loadAllocations();


    } catch (error) {

        console.error(
            "Complete allocation error:",
            error
        );


        alert(
            error.message
        );
    }
}


// ==========================================
// STATUS BADGE
// ==========================================

function getStatusBadge(status) {

    if (!status) {

        return `
            <span class="status-badge">
                UNKNOWN
            </span>
        `;
    }


    const normalizedStatus =
        status.toUpperCase();


    let className =
        "status-badge";


    if (normalizedStatus === "PENDING") {

        className +=
            " status-pending";

    } else if (
        normalizedStatus === "ACCEPTED"
    ) {

        className +=
            " status-accepted";

    } else if (
        normalizedStatus === "REJECTED"
    ) {

        className +=
            " status-rejected";

    } else if (
        normalizedStatus === "ALLOCATED"
    ) {

        className +=
            " status-allocated";

    } else if (
        normalizedStatus === "COMPLETED"
    ) {

        className +=
            " status-completed";
    }


    return `
        <span class="${className}">
            ${normalizedStatus}
        </span>
    `;
}


// ==========================================
// DATE FORMAT
// ==========================================

function formatDate(dateString) {

    if (!dateString) {
        return "-";
    }


    const date =
        new Date(dateString);


    if (Number.isNaN(date.getTime())) {
        return dateString;
    }


    return date.toLocaleDateString(
        "en-IN",
        {
            day: "2-digit",
            month: "short",
            year: "numeric"
        }
    );
}


// ==========================================
// LOGOUT
// ==========================================

function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userId");
    localStorage.removeItem("fullName");

    window.location.href =
        "index.html";
}