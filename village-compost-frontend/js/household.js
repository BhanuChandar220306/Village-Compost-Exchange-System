document.addEventListener("DOMContentLoaded", () => {

    checkHouseholdLogin();

    loadHouseholdData();

    setupWasteRequestForm();

    setupRefreshButton();

    setupLogout();

    setMinimumPickupDate();

});


/* ==========================================
   CHECK LOGIN
========================================== */

function checkHouseholdLogin() {

    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    if (!token || role !== "HOUSEHOLD") {

        window.location.href = "index.html";

        return;
    }

}


/* ==========================================
   LOAD ALL HOUSEHOLD DATA
========================================== */

async function loadHouseholdData() {

    await Promise.all([
        loadWasteRequests(),
        loadRewardPoints(),
        loadUserName()
    ]);

}


/* ==========================================
   LOAD WASTE REQUESTS
========================================== */

async function loadWasteRequests() {

    const tableBody =
        document.getElementById("requestsTableBody");

    try {

        const response =
            await apiRequest("/api/waste/my");

        const requests =
            extractData(response) || [];

        updateRequestStatistics(requests);

        displayWasteRequests(requests);

    } catch (error) {

        console.error(
            "Failed to load waste requests:",
            error
        );

        tableBody.innerHTML = `
            <tr>
                <td colspan="6" class="empty-row">
                    Failed to load waste requests.
                </td>
            </tr>
        `;

    }

}


/* ==========================================
   UPDATE REQUEST STATISTICS
========================================== */

function updateRequestStatistics(requests) {

    const total =
        requests.length;

    const pending =
        requests.filter(
            request =>
                String(request.status).toUpperCase() === "PENDING"
        ).length;

    const collected =
        requests.filter(
            request =>
                String(request.status).toUpperCase() === "COLLECTED"
        );

    const collectedWeight =
        collected.reduce(
            (sum, request) =>
                sum + Number(request.estimatedWeight || 0),
            0
        );

    document.getElementById("totalRequests")
        .textContent = total;

    document.getElementById("pendingRequests")
        .textContent = pending;

    document.getElementById("collectedWaste")
        .textContent =
            `${collectedWeight.toFixed(1)} kg`;
}


/* ==========================================
   DISPLAY WASTE REQUESTS
========================================== */

function displayWasteRequests(requests) {

    const tableBody =
        document.getElementById("requestsTableBody");

    if (!requests.length) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="6" class="empty-row">
                    You have not created any waste requests yet.
                </td>
            </tr>
        `;

        return;
    }


    tableBody.innerHTML =
        requests.map(request => {

            const status =
                String(request.status || "UNKNOWN")
                    .toUpperCase();

            return `
                <tr>

                    <td>
                        #${request.id}
                    </td>

                    <td>
                        ${escapeHtml(
                            request.wasteType || "-"
                        )}
                    </td>

                    <td>
                        ${Number(
                            request.estimatedWeight || 0
                        ).toFixed(1)} kg
                    </td>

                    <td>
                        ${formatDate(
                            request.pickupDate
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            request.description || "-"
                        )}
                    </td>

                    <td>
                        ${createStatusBadge(status)}
                    </td>

                </tr>
            `;

        }).join("");

}


/* ==========================================
   CREATE WASTE REQUEST
========================================== */

function setupWasteRequestForm() {

    const form =
        document.getElementById("wasteRequestForm");

    form.addEventListener(
        "submit",
        async (event) => {

            event.preventDefault();

            const wasteType =
                document.getElementById(
                    "wasteType"
                ).value;

            const estimatedWeight =
                Number(
                    document.getElementById(
                        "estimatedWeight"
                    ).value
                );

            const pickupDate =
                document.getElementById(
                    "pickupDate"
                ).value;

            const description =
                document.getElementById(
                    "description"
                ).value.trim();

            const message =
                document.getElementById(
                    "wasteRequestMessage"
                );


            message.className = "message success";

            message.textContent =
                "Submitting waste request...";


            try {

                await apiRequest(
                    "/api/waste",
                    {
                        method: "POST",

                        body: JSON.stringify({
                            wasteType:
                                wasteType,

                            estimatedWeight:
                                estimatedWeight,

                            pickupDate:
                                pickupDate,

                            description:
                                description,

                            imageUrl:
                                null
                        })
                    }
                );


                message.className =
                    "message success";

                message.textContent =
                    "Waste request created successfully.";


                form.reset();

                setMinimumPickupDate();

                await loadWasteRequests();

            } catch (error) {

                console.error(
                    "Create waste request error:",
                    error
                );

                message.className =
                    "message error";

                message.textContent =
                    error.message ||
                    "Failed to create waste request.";

            }

        }
    );

}


/* ==========================================
   REFRESH
========================================== */

function setupRefreshButton() {

    document
        .getElementById("refreshRequestsButton")
        .addEventListener(
            "click",
            loadWasteRequests
        );

}


/* ==========================================
   LOAD REWARD POINTS
========================================== */

async function loadRewardPoints() {

    try {

        const response =
            await apiRequest(
                "/api/rewards/my"
            );

        const data =
            extractData(response);

        let points = 0;


        if (typeof data === "number") {

            points = data;

        } else if (
            data &&
            typeof data === "object"
        ) {

            points =
                data.rewardPoints ??
                data.points ??
                data.totalPoints ??
                data.balance ??
                0;
        }


        document.getElementById(
            "rewardPoints"
        ).textContent = points;

    } catch (error) {

        console.error(
            "Failed to load reward points:",
            error
        );

        document.getElementById(
            "rewardPoints"
        ).textContent = "0";

    }

}


/* ==========================================
   LOAD USER NAME
========================================== */

async function loadUserName() {

    /*
     * Login already stores the role.
     * If your login response contains fullName,
     * we can display it later.
     */

    const welcome =
        document.getElementById(
            "welcomeUser"
        );

    const storedName =
        localStorage.getItem("fullName");

    if (storedName) {

        welcome.textContent =
            `Hello, ${storedName}`;

    } else {

        welcome.textContent =
            "Household";

    }

}


/* ==========================================
   LOGOUT
========================================== */

function setupLogout() {

    document
        .getElementById("logoutButton")
        .addEventListener(
            "click",
            () => {

                localStorage.removeItem("token");

                localStorage.removeItem("role");

                localStorage.removeItem("userId");

                localStorage.removeItem("fullName");

                window.location.href =
                    "index.html";

            }
        );

}


/* ==========================================
   MINIMUM PICKUP DATE
========================================== */

function setMinimumPickupDate() {

    const input =
        document.getElementById(
            "pickupDate"
        );

    if (!input) {
        return;
    }

    const today =
        new Date();

    const year =
        today.getFullYear();

    const month =
        String(
            today.getMonth() + 1
        ).padStart(2, "0");

    const day =
        String(
            today.getDate()
        ).padStart(2, "0");

    input.min =
        `${year}-${month}-${day}`;

}


/* ==========================================
   EXTRACT API DATA
========================================== */

function extractData(response) {

    if (!response) {
        return null;
    }

    if (
        Object.prototype.hasOwnProperty.call(
            response,
            "data"
        )
    ) {

        return response.data;

    }

    return response;
}


/* ==========================================
   STATUS BADGE
========================================== */

function createStatusBadge(status) {

    const normalized =
        String(status)
            .toLowerCase();

    let className =
        "status-default";

    if (normalized === "pending") {

        className =
            "status-pending";

    } else if (normalized === "accepted") {

        className =
            "status-accepted";

    } else if (normalized === "collected") {

        className =
            "status-collected";

    } else if (normalized === "rejected") {

        className =
            "status-rejected";
    }

    return `
        <span class="status-badge ${className}">
            ${escapeHtml(status)}
        </span>
    `;
}


/* ==========================================
   FORMAT DATE
========================================== */

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


/* ==========================================
   ESCAPE HTML
========================================== */

function escapeHtml(value) {

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");

}