document.addEventListener(
    "DOMContentLoaded",
    () => {

        if (
            document.getElementById(
                "notificationsContainer"
            )
        ) {

            loadNotifications();

        }

    }
);


/* ==========================================
   LOAD NOTIFICATIONS
========================================== */

async function loadNotifications() {

    const container =
        document.getElementById(
            "notificationsContainer"
        );

    try {

        const response =
            await apiRequest(
                "/api/notifications/my"
            );

        const notifications =
            extractNotificationData(response) || [];

        displayNotifications(
            notifications
        );

        updateUnreadCount(
            notifications
        );

    } catch (error) {

        console.error(
            "Failed to load notifications:",
            error
        );

        container.innerHTML = `
            <div class="notification-empty">
                Failed to load notifications.
            </div>
        `;

        document.getElementById(
            "unreadCount"
        ).textContent =
            "0 unread";

    }

}


/* ==========================================
   DISPLAY NOTIFICATIONS
========================================== */

function displayNotifications(
    notifications
) {

    const container =
        document.getElementById(
            "notificationsContainer"
        );

    if (!notifications.length) {

        container.innerHTML = `
            <div class="notification-empty">
                No notifications yet.
            </div>
        `;

        return;
    }


    container.innerHTML =
        notifications.map(
            notification => {

                const unread =
                    !notification.read;

                return `
                    <div
                        class="notification-item ${
                            unread ? "unread" : ""
                        }">

                        <div class="notification-message">

                            ${escapeNotificationHtml(
                                notification.message ||
                                "Notification"
                            )}

                        </div>

                        <div class="notification-date">

                            ${formatNotificationDate(
                                notification.createdAt
                            )}

                        </div>

                    </div>
                `;

            }
        ).join("");

}


/* ==========================================
   UNREAD COUNT
========================================== */

function updateUnreadCount(
    notifications
) {

    const unread =
        notifications.filter(
            notification =>
                !notification.read
        ).length;

    document.getElementById(
        "unreadCount"
    ).textContent =
        `${unread} unread`;

}


/* ==========================================
   EXTRACT DATA
========================================== */

function extractNotificationData(
    response
) {

    if (!response) {
        return null;
    }

    if (
        Array.isArray(response)
    ) {

        return response;

    }

    if (
        Array.isArray(response.data)
    ) {

        return response.data;

    }

    return [];

}


/* ==========================================
   FORMAT DATE
========================================== */

function formatNotificationDate(
    dateString
) {

    if (!dateString) {
        return "";
    }

    const date =
        new Date(dateString);

    if (
        Number.isNaN(
            date.getTime()
        )
    ) {

        return dateString;

    }

    return date.toLocaleString(
        "en-IN",
        {
            day: "2-digit",
            month: "short",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit"
        }
    );

}


/* ==========================================
   ESCAPE HTML
========================================== */

function escapeNotificationHtml(
    value
) {

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");

}