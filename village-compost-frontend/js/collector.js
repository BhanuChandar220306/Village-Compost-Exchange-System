/* =========================================
   COLLECTOR DASHBOARD
   ========================================= */

document.addEventListener("DOMContentLoaded", () => {

    checkCollectorLogin();

    if (!isCollectorLoggedIn()) {
        return;
    }

    setupEventListeners();

    setMinimumDates();

    loadCollectorDashboard();

});


/* =========================================
   AUTHENTICATION
   ========================================= */

function isCollectorLoggedIn() {

    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    return token && role === "COLLECTOR";
}


function checkCollectorLogin() {

    if (!isCollectorLoggedIn()) {

        window.location.href = "index.html";

        return;
    }

    const fullName = localStorage.getItem("fullName");

    const welcomeUser =
        document.getElementById("welcomeUser");

    if (welcomeUser && fullName) {

        welcomeUser.textContent =
            `Welcome, ${fullName}`;
    }
}


/* =========================================
   EVENT LISTENERS
   ========================================= */

function setupEventListeners() {

    const logoutBtn =
        document.getElementById("logoutBtn");

    if (logoutBtn) {

        logoutBtn.addEventListener(
            "click",
            logoutCollector
        );
    }


    const refreshRequestsBtn =
        document.getElementById("refreshRequestsBtn");

    if (refreshRequestsBtn) {

        refreshRequestsBtn.addEventListener(
            "click",
            loadPendingRequests
        );
    }


    const refreshAcceptedBtn =
        document.getElementById("refreshAcceptedBtn");

    if (refreshAcceptedBtn) {

        refreshAcceptedBtn.addEventListener(
            "click",
            loadAcceptedRequests
        );
    }


    const refreshPickupsBtn =
        document.getElementById("refreshPickupsBtn");

    if (refreshPickupsBtn) {

        refreshPickupsBtn.addEventListener(
            "click",
            loadMyPickups
        );
    }


    const pickupForm =
        document.getElementById("pickupForm");

    if (pickupForm) {

        pickupForm.addEventListener(
            "submit",
            schedulePickup
        );
    }

}


/* =========================================
   DASHBOARD
   ========================================= */

async function loadCollectorDashboard() {

    await Promise.all([
        loadPendingRequests(),
        loadAcceptedRequests(),
        loadMyPickups()
    ]);

}


/* =========================================
   PENDING WASTE REQUESTS
   ========================================= */

async function loadPendingRequests() {

    const tableBody =
        document.getElementById(
            "pendingRequestsBody"
        );

    if (!tableBody) {
        return;
    }

    tableBody.innerHTML = `
        <tr>
            <td colspan="8" class="empty-message">
                Loading pending requests...
            </td>
        </tr>
    `;

    try {

        const response =
            await apiRequest(
                "/api/waste/pending"
            );

        const requests =
            extractData(response);

        const pendingRequests =
            Array.isArray(requests)
                ? requests
                : [];

        displayPendingRequests(
            pendingRequests
        );

        updatePendingCount(
            pendingRequests.length
        );

    } catch (error) {

        console.error(
            "Error loading pending requests:",
            error
        );

        tableBody.innerHTML = `
            <tr>
                <td colspan="8" class="empty-message">
                    Failed to load pending requests.
                </td>
            </tr>
        `;

        showMessage(
            "requestMessage",
            error.message,
            "error"
        );
    }

}


/* =========================================
   DISPLAY PENDING REQUESTS
   ========================================= */

function displayPendingRequests(requests) {

    const tableBody =
        document.getElementById(
            "pendingRequestsBody"
        );

    if (!requests.length) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="8" class="empty-message">
                    No pending waste requests.
                </td>
            </tr>
        `;

        return;
    }


    tableBody.innerHTML =
        requests.map(request => {

            const id = request.id ?? "-";

            const householdName =
                request.householdName ?? "-";

            const wasteType =
                request.wasteType ?? "-";

            const weight =
                request.estimatedWeight ?? "-";

            const pickupDate =
                request.pickupDate ?? "-";

            const description =
                request.description ?? "-";

            const status =
                request.status ?? "PENDING";


            return `
                <tr>

                    <td>${escapeHtml(id)}</td>

                    <td>
                        ${escapeHtml(householdName)}
                    </td>

                    <td>
                        ${escapeHtml(wasteType)}
                    </td>

                    <td>
                        ${escapeHtml(weight)} kg
                    </td>

                    <td>
                        ${formatDate(pickupDate)}
                    </td>

                    <td>
                        ${createStatusBadge(status)}
                    </td>

                    <td>
                        ${escapeHtml(description)}
                    </td>

                    <td>

                        <button
                            class="table-action-btn"
                            onclick="acceptWasteRequest(${id})">

                            Accept

                        </button>

                    </td>

                </tr>
            `;

        }).join("");

}


/* =========================================
   ACCEPT WASTE REQUEST
   ========================================= */

async function acceptWasteRequest(requestId) {

    if (!requestId) {
        return;
    }


    const confirmed =
        confirm(
            `Accept waste request #${requestId}?`
        );


    if (!confirmed) {
        return;
    }


    showMessage(
        "requestMessage",
        "Accepting request...",
        ""
    );


    try {

        const response =
            await apiRequest(
                `/api/waste/${requestId}/accept`,
                {
                    method: "PUT"
                }
            );


        console.log(
            "Accept request response:",
            response
        );


        showMessage(
            "requestMessage",
            "Waste request accepted successfully.",
            "success"
        );


        await loadCollectorDashboard();


    } catch (error) {

        console.error(
            "Error accepting request:",
            error
        );


        showMessage(
            "requestMessage",
            error.message,
            "error"
        );
    }

}


/* =========================================
   ACCEPTED REQUESTS
   ========================================= */

async function loadAcceptedRequests() {

    const tableBody =
        document.getElementById(
            "acceptedRequestsBody"
        );

    if (!tableBody) {
        return;
    }


    tableBody.innerHTML = `
        <tr>
            <td colspan="7" class="empty-message">
                Loading accepted requests...
            </td>
        </tr>
    `;


    try {

        /*
         * We use /api/waste/all here because the current
         * backend provides pending requests separately,
         * but does not provide a dedicated
         * /api/waste/accepted endpoint.
         */

        const response =
            await apiRequest(
                "/api/waste/all"
            );


        const requests =
            extractData(response);


        const allRequests =
            Array.isArray(requests)
                ? requests
                : [];


        const acceptedRequests =
            allRequests.filter(request => {

                const status =
                    String(
                        request.status ?? ""
                    ).toUpperCase();

                return status === "ACCEPTED";

            });


        displayAcceptedRequests(
            acceptedRequests
        );


        updateAcceptedCount(
            acceptedRequests.length
        );


    } catch (error) {

        console.error(
            "Error loading accepted requests:",
            error
        );


        tableBody.innerHTML = `
            <tr>
                <td colspan="7" class="empty-message">
                    Failed to load accepted requests.
                </td>
            </tr>
        `;

    }

}


/* =========================================
   DISPLAY ACCEPTED REQUESTS
   ========================================= */

function displayAcceptedRequests(requests) {

    const tableBody =
        document.getElementById(
            "acceptedRequestsBody"
        );


    if (!requests.length) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="7" class="empty-message">
                    No accepted requests.
                </td>
            </tr>
        `;

        return;
    }


    tableBody.innerHTML =
        requests.map(request => {

            const id =
                request.id ?? "-";

            const householdName =
                request.householdName ?? "-";

            const wasteType =
                request.wasteType ?? "-";

            const weight =
                request.estimatedWeight ?? "-";

            const pickupDate =
                request.pickupDate ?? "-";

            const status =
                request.status ?? "ACCEPTED";


            return `
                <tr>

                    <td>
                        ${escapeHtml(id)}
                    </td>

                    <td>
                        ${escapeHtml(householdName)}
                    </td>

                    <td>
                        ${escapeHtml(wasteType)}
                    </td>

                    <td>
                        ${escapeHtml(weight)} kg
                    </td>

                    <td>
                        ${formatDate(pickupDate)}
                    </td>

                    <td>
                        ${createStatusBadge(status)}
                    </td>

                    <td>

                        <button
                            class="table-action-btn"
                            onclick="selectRequestForPickup(${id})">

                            Schedule

                        </button>

                    </td>

                </tr>
            `;

        }).join("");

}


/* =========================================
   SELECT REQUEST FOR PICKUP
   ========================================= */

function selectRequestForPickup(requestId) {

    const input =
        document.getElementById(
            "wasteRequestId"
        );


    if (!input) {
        return;
    }


    input.value = requestId;


    const scheduleSection =
        document.getElementById(
            "scheduleSection"
        );


    if (scheduleSection) {

        scheduleSection.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });

    }

}


/* =========================================
   SCHEDULE PICKUP
   ========================================= */

async function schedulePickup(event) {

    event.preventDefault();


    const wasteRequestId =
        document.getElementById(
            "wasteRequestId"
        ).value;


    const pickupDate =
        document.getElementById(
            "pickupDate"
        ).value;


    const pickupTime =
        document.getElementById(
            "pickupTime"
        ).value;


    const collectorId =
        localStorage.getItem(
            "userId"
        );


    if (!wasteRequestId) {

        showMessage(
            "pickupMessage",
            "Please enter the waste request ID.",
            "error"
        );

        return;
    }


    if (!pickupDate) {

        showMessage(
            "pickupMessage",
            "Please select a pickup date.",
            "error"
        );

        return;
    }


    if (!pickupTime) {

        showMessage(
            "pickupMessage",
            "Please select a pickup time.",
            "error"
        );

        return;
    }


    if (!collectorId) {

        showMessage(
            "pickupMessage",
            "Collector ID is missing. Please login again.",
            "error"
        );

        return;
    }


    const requestBody = {

        wasteRequestId:
            Number(wasteRequestId),

        collectorId:
            Number(collectorId),

        pickupDate:
            pickupDate,

        pickupTime:
            pickupTime

    };


    showMessage(
        "pickupMessage",
        "Scheduling pickup...",
        ""
    );


    try {

        const response =
            await apiRequest(
                "/api/pickup/create",
                {
                    method: "POST",
                    body: JSON.stringify(
                        requestBody
                    )
                }
            );


        console.log(
            "Pickup creation response:",
            response
        );


        showMessage(
            "pickupMessage",
            "Pickup scheduled successfully.",
            "success"
        );


        document
            .getElementById("pickupForm")
            .reset();


        await loadCollectorDashboard();


    } catch (error) {

        console.error(
            "Error scheduling pickup:",
            error
        );


        showMessage(
            "pickupMessage",
            error.message,
            "error"
        );
    }

}


/* =========================================
   MY PICKUPS
   ========================================= */

async function loadMyPickups() {

    const tableBody =
        document.getElementById(
            "pickupsBody"
        );


    if (!tableBody) {
        return;
    }


    tableBody.innerHTML = `
        <tr>
            <td colspan="7" class="empty-message">
                Loading pickups...
            </td>
        </tr>
    `;


    try {

        const response =
            await apiRequest(
                "/api/pickup/my"
            );


        const pickups =
            extractData(response);


        const pickupList =
            Array.isArray(pickups)
                ? pickups
                : [];


        displayPickups(
            pickupList
        );


        updatePickupCounts(
            pickupList
        );


    } catch (error) {

        console.error(
            "Error loading pickups:",
            error
        );


        tableBody.innerHTML = `
            <tr>
                <td colspan="7" class="empty-message">
                    Failed to load pickups.
                </td>
            </tr>
        `;

    }

}


/* =========================================
   DISPLAY PICKUPS
   ========================================= */

function displayPickups(pickups) {

    const tableBody =
        document.getElementById(
            "pickupsBody"
        );


    if (!pickups.length) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="7" class="empty-message">
                    No scheduled pickups found.
                </td>
            </tr>
        `;

        return;
    }


    tableBody.innerHTML =
        pickups.map(pickup => {

            const id =
                pickup.id ?? "-";


            const wasteRequestId =
                pickup.wasteRequestId ??
                pickup.wasteRequest?.id ??
                "-";


            const householdName =
                pickup.householdName ??
                pickup.wasteRequest?.householdName ??
                "-";


            const pickupDate =
                pickup.pickupDate ?? "-";


            const pickupTime =
                pickup.pickupTime ?? "-";


            const status =
                pickup.status ?? "UNKNOWN";


            const normalizedStatus =
                String(status)
                    .toUpperCase();


            let action = "";


            if (
                normalizedStatus !== "COLLECTED" &&
                normalizedStatus !== "COMPLETED"
            ) {

                action = `
                    <button
                        class="table-action-btn"
                        onclick="completePickup(${id})">

                        Complete

                    </button>
                `;

            } else {

                action = `
                    <span class="status-badge status-completed">
                        Completed
                    </span>
                `;

            }


            return `
                <tr>

                    <td>
                        ${escapeHtml(id)}
                    </td>

                    <td>
                        ${escapeHtml(wasteRequestId)}
                    </td>

                    <td>
                        ${escapeHtml(householdName)}
                    </td>

                    <td>
                        ${formatDate(pickupDate)}
                    </td>

                    <td>
                        ${escapeHtml(pickupTime)}
                    </td>

                    <td>
                        ${createStatusBadge(status)}
                    </td>

                    <td>
                        ${action}
                    </td>

                </tr>
            `;

        }).join("");

}


/* =========================================
   COMPLETE PICKUP
   ========================================= */

async function completePickup(pickupId) {

    if (!pickupId) {
        return;
    }


    const confirmed =
        confirm(
            `Complete pickup #${pickupId}?`
        );


    if (!confirmed) {
        return;
    }


    showMessage(
        "completeMessage",
        "Completing pickup...",
        ""
    );


    try {

        const response =
            await apiRequest(
                `/api/pickup/${pickupId}/complete`,
                {
                    method: "PUT"
                }
            );


        console.log(
            "Complete pickup response:",
            response
        );


        showMessage(
            "completeMessage",
            "Pickup completed successfully.",
            "success"
        );


        await loadCollectorDashboard();


    } catch (error) {

        console.error(
            "Error completing pickup:",
            error
        );


        showMessage(
            "completeMessage",
            error.message,
            "error"
        );

    }

}


/* =========================================
   COUNTS
   ========================================= */

function updatePendingCount(count) {

    const element =
        document.getElementById(
            "pendingCount"
        );

    if (element) {

        element.textContent =
            count;
    }

}


function updateAcceptedCount(count) {

    const element =
        document.getElementById(
            "acceptedCount"
        );

    if (element) {

        element.textContent =
            count;
    }

}


function updatePickupCounts(pickups) {

    const scheduledElement =
        document.getElementById(
            "scheduledCount"
        );


    const completedElement =
        document.getElementById(
            "completedCount"
        );


    let completed = 0;


    pickups.forEach(pickup => {

        const status =
            String(
                pickup.status ?? ""
            ).toUpperCase();


        if (
            status === "COLLECTED" ||
            status === "COMPLETED"
        ) {

            completed++;

        }

    });


    if (scheduledElement) {

        scheduledElement.textContent =
            pickups.length;

    }


    if (completedElement) {

        completedElement.textContent =
            completed;

    }

}


/* =========================================
   DATE SETTINGS
   ========================================= */

function setMinimumDates() {

    const today =
        new Date()
            .toISOString()
            .split("T")[0];


    const pickupDate =
        document.getElementById(
            "pickupDate"
        );


    if (pickupDate) {

        pickupDate.min =
            today;

    }

}


/* =========================================
   LOGOUT
   ========================================= */

function logoutCollector() {

    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userId");
    localStorage.removeItem("fullName");

    window.location.href =
        "index.html";

}


/* =========================================
   RESPONSE DATA HELPER
   ========================================= */

function extractData(response) {

    if (
        response &&
        typeof response === "object" &&
        "data" in response
    ) {

        return response.data;

    }

    return response;

}


/* =========================================
   STATUS BADGE
   ========================================= */

function createStatusBadge(status) {

    const normalizedStatus =
        String(
            status ?? "UNKNOWN"
        ).toUpperCase();


    let className =
        "status-badge";


    if (
        normalizedStatus === "PENDING"
    ) {

        className +=
            " status-pending";

    } else if (
        normalizedStatus === "ACCEPTED"
    ) {

        className +=
            " status-accepted";

    } else if (
        normalizedStatus === "SCHEDULED"
    ) {

        className +=
            " status-scheduled";

    } else if (
        normalizedStatus === "COLLECTED"
    ) {

        className +=
            " status-collected";

    } else if (
        normalizedStatus === "COMPLETED"
    ) {

        className +=
            " status-completed";

    } else if (
        normalizedStatus === "REJECTED" ||
        normalizedStatus === "CANCELLED"
    ) {

        className +=
            " status-rejected";

    }


    return `
        <span class="${className}">
            ${escapeHtml(normalizedStatus)}
        </span>
    `;

}


/* =========================================
   FORMAT DATE
   ========================================= */

function formatDate(dateValue) {

    if (!dateValue) {
        return "-";
    }


    const date =
        new Date(dateValue);


    if (Number.isNaN(date.getTime())) {

        return escapeHtml(
            dateValue
        );

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


/* =========================================
   ESCAPE HTML
   ========================================= */

function escapeHtml(value) {

    if (value === null ||
        value === undefined) {

        return "";

    }


    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");

}


/* =========================================
   MESSAGE HELPER
   ========================================= */

function showMessage(
    elementId,
    message,
    type
) {

    const element =
        document.getElementById(
            elementId
        );


    if (!element) {
        return;
    }


    element.textContent =
        message;


    element.className =
        "message";


    if (type) {

        element.classList.add(
            type
        );

    }

}