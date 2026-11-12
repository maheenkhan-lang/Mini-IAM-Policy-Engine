const token = sessionStorage.getItem("token");

if (!token) {
    window.location.href = "index.html";
}

// Display logged-in user
const username = sessionStorage.getItem("username") || "User";
const department = sessionStorage.getItem("department") || "Department";

document.getElementById("displayUsername").textContent = username;
document.getElementById("displayDepartment").textContent = department;
document.getElementById("welcomeName").textContent = username;
document.getElementById("userAvatar").textContent =
    username.charAt(0).toUpperCase();

// Sidebar navigation
const navLinks = document.querySelectorAll(".nav-link");
const sections = document.querySelectorAll(".content-section");
const pageTitle = document.getElementById("pageTitle");

function showSection(sectionId) {

    sections.forEach(section => {
        section.classList.remove("active");
    });

    navLinks.forEach(link => {
        link.classList.remove("active");
    });

    const selectedSection = document.getElementById(sectionId);

    if (!selectedSection) return;

    selectedSection.classList.add("active");

    const selectedLink = document.querySelector(
        `.nav-link[data-section="${sectionId}"]`
    );

    if (selectedLink) {
        selectedLink.classList.add("active");
        pageTitle.textContent = selectedLink.textContent.trim();
    }

    window.location.hash = sectionId;
}

navLinks.forEach(link => {
    link.addEventListener("click", function(event) {
        event.preventDefault();
        showSection(this.dataset.section);
    });
});

// Quick access buttons
document.querySelectorAll(".quick-card").forEach(button => {
    button.addEventListener("click", function() {
        showSection(this.dataset.go);
    });
});

// Open section from URL hash
const initialSection = window.location.hash.substring(1);
if (initialSection && document.getElementById(initialSection)) {
    showSection(initialSection);
}

// Logout
document.getElementById("logoutButton").addEventListener("click", function() {

    sessionStorage.clear();
    window.location.href = "index.html";

});

// Load users
async function loadUsers() {

    try {

        const response = await fetch("/api/users", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Unable to load users");
        }

        const users = await response.json();

        document.getElementById("totalUsers").textContent = users.length;

        const table = document.getElementById("usersTable");
        table.innerHTML = "";

        users.forEach(user => {

            const row = document.createElement("tr");

            const values = [
                user.id,
                user.username,
                user.email,
                user.department,
                user.roles?.map(role => role.name).join(", ") || "No role"
            ];

            values.forEach(value => {
                const cell = document.createElement("td");
                cell.textContent = value ?? "-";
                row.appendChild(cell);
            });

            const actionCell = document.createElement("td");

            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Delete";
            deleteButton.style.width = "auto";
            deleteButton.style.margin = "0";
            deleteButton.style.padding = "7px 12px";
            deleteButton.style.background = "#dc2626";

            deleteButton.addEventListener("click", () => {
                deleteUser(user.id);
            });

            actionCell.appendChild(deleteButton);
            row.appendChild(actionCell);

            table.appendChild(row);

        });

    } catch (error) {

        console.error(error);
        document.getElementById("totalUsers").textContent = "-";

    }
}

// Create user and assign role
document.getElementById("addUserForm").addEventListener("submit", async function(event) {

    event.preventDefault();

    const message = document.getElementById("userMessage");
    message.textContent = "Creating user...";

    const user = {
        username: document.getElementById("newUsername").value,
        email: document.getElementById("newEmail").value,
        department: document.getElementById("newDepartment").value,
        password: document.getElementById("newPassword").value
    };

    const roleId = document.getElementById("newUserRole").value;

    try {

        const response = await fetch("/api/users", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify(user)
        });

        if (!response.ok) {
            throw new Error("Could not create user");
        }

        const createdUser = await response.json();

        const roleResponse = await fetch(
            `/api/users/${createdUser.id}/roles/${roleId}`,
            {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (!roleResponse.ok) {
            throw new Error("User created, but role assignment failed");
        }

        message.textContent = "User created and role assigned successfully!";
        message.style.color = "#4ade80";

        this.reset();

        loadUsers();

    } catch (error) {

        message.textContent = error.message;
        message.style.color = "#f87171";

    }

});

// Delete user
async function deleteUser(userId) {

    if (!confirm("Are you sure you want to delete this user?")) {
        return;
    }

    try {

        const response = await fetch(`/api/users/${userId}`, {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Could not delete user");
        }

        loadUsers();

    } catch (error) {

        alert(error.message);

    }
}

loadUsers();

// Policy checker
document.getElementById("policyForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    const action = document.getElementById("policyAction").value;
    const resource = document.getElementById("policyResource").value;
    const resultBox = document.getElementById("policyResult");

    resultBox.className = "policy-result";
    resultBox.textContent = "Evaluating access...";

    try {
        const response = await fetch(
            `/api/policy/check?action=${action}&resource=${resource}`,
            {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        const result = await response.json();

        resultBox.className = result.allowed
            ? "policy-result allowed"
            : "policy-result denied";

        resultBox.innerHTML = `
            <h3>${result.allowed ? "ACCESS GRANTED" : "ACCESS DENIED"}</h3>
            <p>${result.reason || "No additional details provided."}</p>
        `;

    } catch (error) {
        resultBox.className = "policy-result denied";
        resultBox.textContent = "Unable to evaluate the policy.";
    }
});
// CLOUD RESOURCE MANAGEMENT

async function loadResources() {

    try {

        const response = await fetch("/api/resources", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Unable to load resources");
        }

        const resources = await response.json();

        document.getElementById("totalResources").textContent =
            resources.length;

        const table = document.getElementById("resourcesTable");
        table.innerHTML = "";

        resources.forEach(resource => {

            const row = document.createElement("tr");

            const values = [
                resource.id,
                resource.name,
                resource.type,
                resource.status,
                resource.region
            ];

            values.forEach(value => {
                const cell = document.createElement("td");
                cell.textContent = value ?? "-";
                row.appendChild(cell);
            });

            const actionCell = document.createElement("td");

            if (resource.type === "VM") {

                const startButton = document.createElement("button");
                startButton.textContent = "Start";
                startButton.className = "resource-action";
                startButton.addEventListener("click", () => {
                    performResourceAction(resource.id, "start");
                });

                const stopButton = document.createElement("button");
                stopButton.textContent = "Stop";
                stopButton.className = "resource-action";
                stopButton.addEventListener("click", () => {
                    performResourceAction(resource.id, "stop");
                });

                actionCell.append(startButton, stopButton);
            }

            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Delete";
            deleteButton.className = "resource-delete";

            deleteButton.addEventListener("click", () => {
                deleteResource(resource.id);
            });

            actionCell.appendChild(deleteButton);
            row.appendChild(actionCell);

            table.appendChild(row);

        });

    } catch (error) {

        console.error(error);
        document.getElementById("totalResources").textContent = "-";

    }
}


// CREATE RESOURCE

document.getElementById("resourceForm").addEventListener("submit", async function(event) {

    event.preventDefault();

    const message = document.getElementById("resourceMessage");

    const resource = {
        name: document.getElementById("resourceName").value,
        type: document.getElementById("resourceType").value,
        region: document.getElementById("resourceRegion").value,
        status: "STOPPED"
    };

    message.textContent = "Creating resource...";

    try {

        const response = await fetch("/api/resources", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify(resource)
        });

        if (!response.ok) {
            throw new Error("Resource creation failed. Check your permissions.");
        }

        message.textContent = "Resource created successfully!";
        message.style.color = "#4ade80";

        this.reset();
        loadResources();

    } catch (error) {

        message.textContent = error.message;
        message.style.color = "#f87171";

    }

});


// START OR STOP VM

async function performResourceAction(resourceId, action) {

    try {

        const response = await fetch(
            `/api/resources/${resourceId}/${action}`,
            {
                method: "PUT",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            throw new Error("Action failed. Check your permissions.");
        }

        loadResources();

    } catch (error) {

        alert(error.message);

    }
}


// DELETE RESOURCE

async function deleteResource(resourceId) {

    if (!confirm("Are you sure you want to delete this resource?")) {
        return;
    }

    try {

        const response = await fetch(`/api/resources/${resourceId}`, {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Could not delete resource. Check your permissions.");
        }

        loadResources();

    } catch (error) {

        alert(error.message);

    }
}


// Initial resource loading

loadResources();
// AUDIT LOG MANAGEMENT

async function loadAuditLogs() {

    try {

        const response = await fetch("/api/audit-logs", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Unable to load audit logs");
        }

        const logs = await response.json();

        const table = document.getElementById("auditTable");

        table.innerHTML = "";

        let allowed = 0;
        let denied = 0;

        logs.forEach(log => {

            if (log.allowed) {
                allowed++;
            } else {
                denied++;
            }

            const row = document.createElement("tr");

            const values = [
                log.username,
                log.action,
                log.resource
            ];

            values.forEach(value => {
                const cell = document.createElement("td");
                cell.textContent = value ?? "-";
                row.appendChild(cell);
            });

            const decisionCell = document.createElement("td");
            decisionCell.textContent = log.allowed ? "ALLOWED" : "DENIED";
            decisionCell.className = log.allowed ? "audit-allowed" : "audit-denied";
            row.appendChild(decisionCell);

            const reasonCell = document.createElement("td");
            reasonCell.textContent = log.reason ?? "-";
            row.appendChild(reasonCell);

            const timeCell = document.createElement("td");

            timeCell.textContent = log.createdAt
                ? new Date(log.createdAt).toLocaleString()
                : "-";

            row.appendChild(timeCell);

            table.appendChild(row);

        });

        document.getElementById("auditTotal").textContent = logs.length;
        document.getElementById("auditAllowed").textContent = allowed;
        document.getElementById("auditDenied").textContent = denied;

        if (logs.length === 0) {
            table.innerHTML = '<tr><td colspan="6">No audit records found.</td></tr>';
        }

    } catch (error) {

        console.error(error);

        document.getElementById("auditTotal").textContent = "-";
        document.getElementById("auditAllowed").textContent = "-";
        document.getElementById("auditDenied").textContent = "-";

    }
}

loadAuditLogs();
// Load total permissions
async function loadPermissions() {
    try {
        const response = await fetch("/api/permissions", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Unable to load permissions");
        }

        const permissions = await response.json();

        document.getElementById("totalPermissions").textContent =
            permissions.length;

    } catch (error) {
        console.error("Permission loading error:", error);
    }
}

loadPermissions();