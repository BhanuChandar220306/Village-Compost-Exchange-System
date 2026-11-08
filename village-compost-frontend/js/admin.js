/* =========================================
   ADMIN DASHBOARD
========================================= */

document.addEventListener("DOMContentLoaded", () => {

    const role = localStorage.getItem("role");

    if (role !== "ADMIN") {
        alert("Access denied. Admin access required.");
        window.location.href = "index.html";
        return;
    }

    loadAdminDashboard();
    loadCompostRequests();
    loadInventory();
    loadAllocations();

    setupEventListeners();

});


/* =========================================
   EVENT LISTENERS
========================================= */

function setupEventListeners() {

    const logoutBtn =
        document.getElementById("logoutBtn");

    if (logoutBtn) {
        logoutBtn.addEventListener(
            "click",
            logout
        );
    }


    const refreshCompostBtn =
        document.getElementById("refreshCompostBtn");

    if (refreshCompostBtn) {
        refreshCompostBtn.addEventListener(
            "click",
            loadCompostRequests
        );
    }


    const refreshInventoryBtn =
        document.getElementById("refreshInventoryBtn");

    if (refreshInventoryBtn) {
        refreshInventoryBtn.addEventListener(
            "click",
            loadInventory
        );
    }


    const refreshAllocationBtn =
        document.getElementById("refreshAllocationBtn");

    if (refreshAllocationBtn) {
        refreshAllocationBtn.addEventListener(
            "click",
            loadAllocations
        );
    }


    const inventoryForm =
        document.getElementById("inventoryForm");

    if (inventoryForm) {

        inventoryForm.addEventListener(
            "submit",
            addInventory
        );

    }


    const allocationForm =
        document.getElementById("allocationForm");

    if (allocationForm) {

        allocationForm.addEventListener(
            "submit",
            createAllocation
        );

    }

}


/* =========================================
   ADMIN DASHBOARD
========================================= */

async function loadAdminDashboard() {

    try {

        const response =
            await apiRequest(
                "/api/dashboard/admin"
            );

        const data = response.data;

        if (!data) {
            console.error(
                "Dashboard data not found"
            );
            return;
        }


        setText(
            "adminName",
            localStorage.getItem("fullName") || "Admin"
        );


        setText(
            "totalUsers",
            data.totalUsers ?? 0
        );

        setText(
            "totalHouseholds",
            data.totalHouseholds ?? 0
        );

        setText(
            "totalFarmers",
            data.totalFarmers ?? 0
        );

        setText(
            "totalCollectors",
            data.totalCollectors ?? 0
        );

        setText(
            "totalWasteRequests",
            data.totalWasteRequests ?? 0
        );

        setText(
            "totalCompostRequests",
            data.totalCompostRequests ?? 0
        );

        setText(
            "pendingCompostRequests",
            data.pendingCompostRequests ?? 0
        );

        setText(
            "acceptedCompostRequests",
            data.acceptedCompostRequests ?? 0
        );

    } catch (error) {

        console.error(
            "Failed to load admin dashboard:",
            error
        );

    }

}


/* =========================================
   COMPOST REQUESTS
========================================= */

async function loadCompostRequests() {

    const tbody =
        document.getElementById(
            "compostRequestsBody"
        );

    if (!tbody) return;


    tbody.innerHTML = `
        <tr>
            <td colspan="6" class="empty-message">
                Loading compost requests...
            </td>
        </tr>
    `;


    try {

        const response =
            await apiRequest(
                "/api/compost/all"
            );

        const requests =
            response.data || [];


        if (requests.length === 0) {

            tbody.innerHTML = `
                <tr>
                    <td
                        colspan="6"
                        class="empty-message">
                        No compost requests found.
                    </td>
                </tr>
            `;

            return;
        }


        tbody.innerHTML =
            requests.map(
                request =>
                    createCompostRow(request)
            ).join("");


    } catch (error) {

        console.error(
            "Failed to load compost requests:",
            error
        );

        tbody.innerHTML = `
            <tr>
                <td
                    colspan="6"
                    class="empty-message">
                    Failed to load compost requests.
                </td>
            </tr>
        `;

    }

}


/* =========================================
   COMPOST TABLE ROW
========================================= */

function createCompostRow(request) {

    const status =
        String(
            request.status || ""
        ).toUpperCase();


    let actions = "-";


    if (status === "PENDING") {

        actions = `
            <button
                class="action-btn approve-btn"
                onclick="approveCompost(${request.id})">
                Approve
            </button>

            <button
                class="action-btn reject-btn"
                onclick="rejectCompost(${request.id})">
                Reject
            </button>
        `;

    }


    return `
        <tr>

            <td>
                ${request.id ?? "-"}
            </td>

            <td>
                ${escapeHtml(
                    request.farmerName || "-"
                )}
            </td>

            <td>
                ${request.quantity ?? "-"}
            </td>

            <td>
                ${formatDate(
                    request.requiredDate
                )}
            </td>

            <td>
                ${statusBadge(status)}
            </td>

            <td>
                ${actions}
            </td>

        </tr>
    `;

}


/* =========================================
   APPROVE COMPOST
========================================= */

async function approveCompost(id) {

    const confirmed =
        confirm(
            "Approve this compost request?"
        );

    if (!confirmed) return;


    try {

        await apiRequest(
            `/api/compost/${id}/approve`,
            {
                method: "PUT"
            }
        );


        alert(
            "Compost request approved successfully."
        );


        await loadCompostRequests();
        await loadAdminDashboard();

    } catch (error) {

        alert(
            error.message ||
            "Failed to approve compost request."
        );

    }

}


/* =========================================
   REJECT COMPOST
========================================= */

async function rejectCompost(id) {

    const confirmed =
        confirm(
            "Reject this compost request?"
        );

    if (!confirmed) return;


    try {

        await apiRequest(
            `/api/compost/${id}/reject`,
            {
                method: "PUT"
            }
        );


        alert(
            "Compost request rejected successfully."
        );


        await loadCompostRequests();
        await loadAdminDashboard();

    } catch (error) {

        alert(
            error.message ||
            "Failed to reject compost request."
        );

    }

}


/* =========================================
   INVENTORY
========================================= */

async function loadInventory() {

    const tbody =
        document.getElementById(
            "inventoryBody"
        );

    if (!tbody) return;


    tbody.innerHTML = `
        <tr>
            <td colspan="5" class="empty-message">
                Loading inventory...
            </td>
        </tr>
    `;


    try {

        const response =
            await apiRequest(
                "/api/inventory/all"
            );

        const inventory =
            response.data || [];


        if (inventory.length === 0) {

            tbody.innerHTML = `
                <tr>
                    <td
                        colspan="5"
                        class="empty-message">
                        No inventory available.
                    </td>
                </tr>
            `;

            return;
        }


        tbody.innerHTML =
            inventory.map(
                item =>
                    createInventoryRow(item)
            ).join("");


    } catch (error) {

        console.error(
            "Failed to load inventory:",
            error
        );

        tbody.innerHTML = `
            <tr>
                <td
                    colspan="5"
                    class="empty-message">
                    Failed to load inventory.
                </td>
            </tr>
        `;

    }

}


/* =========================================
   INVENTORY ROW
========================================= */

function createInventoryRow(item) {

    return `
        <tr>

            <td>
                ${item.id ?? "-"}
            </td>

            <td>
                ${item.availableQuantity ?? 0}
            </td>

            <td>
                ${escapeHtml(
                    item.compostType || "-"
                )}
            </td>

            <td>
                ${escapeHtml(
                    item.location || "-"
                )}
            </td>

            <td>

                <button
                    class="action-btn delete-btn"
                    onclick="deleteInventory(${item.id})">

                    Delete

                </button>

            </td>

        </tr>
    `;

}


/* =========================================
   ADD INVENTORY
========================================= */

async function addInventory(event) {

    event.preventDefault();


    const availableQuantity =
        Number(
            document.getElementById(
                "availableQuantity"
            ).value
        );


    const compostType =
        document.getElementById(
            "compostType"
        ).value.trim();


    const location =
        document.getElementById(
            "location"
        ).value.trim();


    if (
        !availableQuantity ||
        availableQuantity <= 0
    ) {

        alert(
            "Please enter a valid quantity."
        );

        return;

    }


    if (!compostType) {

        alert(
            "Please enter the compost type."
        );

        return;

    }


    if (!location) {

        alert(
            "Please enter the location."
        );

        return;

    }


    try {

        await apiRequest(
            "/api/inventory/add",
            {
                method: "POST",

                body: JSON.stringify({
                    availableQuantity,
                    compostType,
                    location
                })
            }
        );


        alert(
            "Inventory added successfully."
        );


        document
            .getElementById(
                "inventoryForm"
            )
            .reset();


        await loadInventory();

    } catch (error) {

        alert(
            error.message ||
            "Failed to add inventory."
        );

    }

}


/* =========================================
   DELETE INVENTORY
========================================= */

async function deleteInventory(id) {

    const confirmed =
        confirm(
            "Delete this inventory item?"
        );

    if (!confirmed) return;


    try {

        await apiRequest(
            `/api/inventory/${id}`,
            {
                method: "DELETE"
            }
        );


        alert(
            "Inventory deleted successfully."
        );


        await loadInventory();

    } catch (error) {

        alert(
            error.message ||
            "Failed to delete inventory."
        );

    }

}


/* =========================================
   ALLOCATIONS
========================================= */

async function loadAllocations() {

    const tbody =
        document.getElementById(
            "allocationBody"
        );

    if (!tbody) return;


    tbody.innerHTML = `
        <tr>
            <td colspan="5" class="empty-message">
                Loading allocations...
            </td>
        </tr>
    `;


    try {

        const response =
            await apiRequest(
                "/api/allocation/all"
            );

        const allocations =
            response.data || [];


        if (allocations.length === 0) {

            tbody.innerHTML = `
                <tr>
                    <td
                        colspan="5"
                        class="empty-message">
                        No allocations found.
                    </td>
                </tr>
            `;

            return;
        }


        tbody.innerHTML =
            allocations.map(
                allocation =>
                    createAllocationRow(
                        allocation
                    )
            ).join("");


    } catch (error) {

        console.error(
            "Failed to load allocations:",
            error
        );

        tbody.innerHTML = `
            <tr>
                <td
                    colspan="5"
                    class="empty-message">
                    Failed to load allocations.
                </td>
            </tr>
        `;

    }

}


/* =========================================
   ALLOCATION ROW
========================================= */

function createAllocationRow(
    allocation
) {

    const status =
        String(
            allocation.status || ""
        ).toUpperCase();


    return `
        <tr>

            <td>
                ${allocation.id ?? "-"}
            </td>

            <td>
                ${escapeHtml(
                    allocation.farmerName || "-"
                )}
            </td>

            <td>
                ${allocation.allocatedQuantity ?? "-"}
            </td>

            <td>
                ${formatDate(
                    allocation.allocatedDate
                )}
            </td>

            <td>
                ${statusBadge(status)}
            </td>

        </tr>
    `;

}


/* =========================================
   CREATE ALLOCATION
========================================= */

async function createAllocation(event) {

    event.preventDefault();


    const compostRequestId =
        Number(
            document.getElementById(
                "allocationRequestId"
            ).value
        );


    const inventoryId =
        Number(
            document.getElementById(
                "allocationInventoryId"
            ).value
        );


    const quantity =
        Number(
            document.getElementById(
                "allocationQuantity"
            ).value
        );


    if (
        !compostRequestId ||
        compostRequestId <= 0
    ) {

        alert(
            "Enter a valid compost request ID."
        );

        return;

    }


    if (
        !inventoryId ||
        inventoryId <= 0
    ) {

        alert(
            "Enter a valid inventory ID."
        );

        return;

    }


    if (
        !quantity ||
        quantity <= 0
    ) {

        alert(
            "Enter a valid quantity."
        );

        return;

    }


    try {

        await apiRequest(
            "/api/allocation/allocate",
            {
                method: "POST",

                body: JSON.stringify({
                    compostRequestId,
                    inventoryId,
                    quantity
                })
            }
        );


        alert(
            "Compost allocated successfully."
        );


        document
            .getElementById(
                "allocationForm"
            )
            .reset();


        await loadAllocations();
        await loadInventory();

    } catch (error) {

        alert(
            error.message ||
            "Failed to allocate compost."
        );

    }

}


/* =========================================
   STATUS BADGE
========================================= */

function statusBadge(status) {

    const normalized =
        String(status || "")
            .toUpperCase();


    let className =
        "status-badge";


    if (normalized === "PENDING") {

        className +=
            " status-pending";

    } else if (
        normalized === "ACCEPTED"
    ) {

        className +=
            " status-accepted";

    } else if (
        normalized === "REJECTED"
    ) {

        className +=
            " status-rejected";

    } else if (
        normalized === "ALLOCATED"
    ) {

        className +=
            " status-allocated";

    } else if (
        normalized === "COMPLETED"
    ) {

        className +=
            " status-completed";

    }


    return `
        <span class="${className}">
            ${escapeHtml(normalized || "-")}
        </span>
    `;

}


/* =========================================
   DATE FORMAT
========================================= */

function formatDate(dateValue) {

    if (!dateValue) {
        return "-";
    }


    const date =
        new Date(dateValue);


    if (Number.isNaN(date.getTime())) {
        return dateValue;
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
   TEXT HELPER
========================================= */

function setText(id, value) {

    const element =
        document.getElementById(id);

    if (element) {

        element.textContent =
            value;

    }

}


/* =========================================
   HTML ESCAPE
========================================= */

function escapeHtml(value) {

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");

}


/* =========================================
   LOGOUT
========================================= */

function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userId");
    localStorage.removeItem("fullName");

    window.location.href =
        "index.html";

}