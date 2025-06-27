document.addEventListener("DOMContentLoaded", function () {
    const refreshButton = document.getElementById("refresh-subscriptions");

    if (refreshButton) {
        refreshButton.addEventListener("click", function () {
            fetch("/api/subscriptions")
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Network response was not ok");
                    }
                    return response.json();
                })
                .then(data => updateSubscriptionList(data))
                .catch(error => {
                    console.error("Error fetching subscriptions:", error);
                });
        });
    }

    function updateSubscriptionList(subscriptions) {
        const tableBody = document.getElementById("subscription-table-body");
        tableBody.innerHTML = ""; // Clear existing rows

        subscriptions.forEach(sub => {
            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${sub.name}</td>
                <td>${sub.startDate}</td>
                <td>${sub.endDate}</td>
                <td>${sub.status}</td>
            `;
            tableBody.appendChild(row);
        });
    }
});