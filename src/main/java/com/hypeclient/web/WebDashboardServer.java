package com.hypeclient.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Web Dashboard Server
 * Runs on localhost:3000
 */
public class WebDashboardServer {
    private static final Logger logger = LoggerFactory.getLogger(WebDashboardServer.class);
    private static final int PORT = 3000;
    
    private ServerSocket serverSocket;

    public WebDashboardServer() {
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            logger.info("🌐 Web Dashboard started on http://localhost:" + PORT);
            
            new Thread(() -> {
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        new Thread(() -> handleClient(clientSocket)).start();
                    } catch (IOException e) {
                        logger.error("Error accepting client connection", e);
                    }
                }
            }).start();
            
        } catch (IOException e) {
            logger.error("Failed to start web dashboard", e);
        }
    }

    private void handleClient(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()),
                true
            );
            
            String requestLine = reader.readLine();
            if (requestLine == null) return;
            
            logger.info("📥 Web Request: " + requestLine);
            
            String[] parts = requestLine.split(" ");
            String path = parts[1];
            
            // Skip headers
            while ((reader.readLine()) != null) {
            }
            
            String response;
            String contentType = "text/html";
            
            if (path.equals("/") || path.equals("/index.html")) {
                response = getIndexHtml();
            } else if (path.equals("/style.css")) {
                response = getStyleCss();
                contentType = "text/css";
            } else if (path.equals("/script.js")) {
                response = getScriptJs();
                contentType = "application/javascript";
            } else {
                response = "404 Not Found";
                contentType = "text/plain";
            }
            
            // Send HTTP response
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: " + contentType + "; charset=utf-8");
            writer.println("Access-Control-Allow-Origin: *");
            writer.println("Content-Length: " + response.getBytes(StandardCharsets.UTF_8).length);
            writer.println();
            writer.print(response);
            writer.flush();
            
            socket.close();
            
        } catch (Exception e) {
            logger.error("Error handling client", e);
        }
    }

    private String getIndexHtml() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>🎮 HYPE CLIENT - Web Dashboard</title>\n" +
                "    <link rel=\"stylesheet\" href=\"/style.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <header>\n" +
                "            <div class=\"header-content\">\n" +
                "                <h1>🎮 HYPE CLIENT</h1>\n" +
                "                <p>Cosmetics Management Dashboard</p>\n" +
                "            </div>\n" +
                "        </header>\n" +
                "\n" +
                "        <main>\n" +
                "            <div class=\"player-section\">\n" +
                "                <h2>👤 Player Selection</h2>\n" +
                "                <input type=\"text\" id=\"playerInput\" placeholder=\"Enter player username...\" class=\"input-field\">\n" +
                "                <button onclick=\"loadPlayer()\" class=\"btn btn-primary\">Load Player</button>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"cosmetics-section\" id=\"cosmeticsSection\" style=\"display: none;\">\n" +
                "                <h2>🎨 Cosmetics Manager</h2>\n" +
                "                <p class=\"player-name\" id=\"playerName\"></p>\n" +
                "\n" +
                "                <div class=\"cosmetics-grid\">\n" +
                "                    <div class=\"cosmetic-category\">\n" +
                "                        <h3>🪶 Wings</h3>\n" +
                "                        <div id=\"wingsContainer\" class=\"cosmetic-list\"></div>\n" +
                "                    </div>\n" +
                "\n" +
                "                    <div class=\"cosmetic-category\">\n" +
                "                        <h3>👑 Hats</h3>\n" +
                "                        <div id=\"hatsContainer\" class=\"cosmetic-list\"></div>\n" +
                "                    </div>\n" +
                "\n" +
                "                    <div class=\"cosmetic-category\">\n" +
                "                        <h3>🐉 Pets</h3>\n" +
                "                        <div id=\"petsContainer\" class=\"cosmetic-list\"></div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "\n" +
                "                <div class=\"active-cosmetics\">\n" +
                "                    <h3>✨ Currently Equipped</h3>\n" +
                "                    <div id=\"activeCosmetics\" class=\"active-list\"></div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"info-section\">\n" +
                "                <h3>ℹ️ API Status</h3>\n" +
                "                <div id=\"apiStatus\" class=\"status-box\">Checking...</div>\n" +
                "            </div>\n" +
                "        </main>\n" +
                "\n" +
                "        <footer>\n" +
                "            <p>🚀 HYPE CLIENT v1.0.0 | API: localhost:8888 | Dashboard: localhost:3000</p>\n" +
                "        </footer>\n" +
                "    </div>\n" +
                "\n" +
                "    <script src=\"/script.js\"></script>\n" +
                "</body>\n" +
                "</html>";
    }

    private String getStyleCss() {
        return "* {\n" +
                "    margin: 0;\n" +
                "    padding: 0;\n" +
                "    box-sizing: border-box;\n" +
                "}\n" +
                "\n" +
                "body {\n" +
                "    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                "    color: #333;\n" +
                "    min-height: 100vh;\n" +
                "    padding: 20px;\n" +
                "}\n" +
                "\n" +
                ".container {\n" +
                "    max-width: 1200px;\n" +
                "    margin: 0 auto;\n" +
                "    background: white;\n" +
                "    border-radius: 20px;\n" +
                "    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);\n" +
                "    overflow: hidden;\n" +
                "}\n" +
                "\n" +
                "header {\n" +
                "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                "    color: white;\n" +
                "    padding: 40px 20px;\n" +
                "    text-align: center;\n" +
                "}\n" +
                "\n" +
                ".header-content h1 {\n" +
                "    font-size: 3em;\n" +
                "    margin-bottom: 10px;\n" +
                "    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);\n" +
                "}\n" +
                "\n" +
                ".header-content p {\n" +
                "    font-size: 1.2em;\n" +
                "    opacity: 0.9;\n" +
                "}\n" +
                "\n" +
                "main {\n" +
                "    padding: 40px;\n" +
                "}\n" +
                "\n" +
                ".player-section {\n" +
                "    background: #f8f9fa;\n" +
                "    padding: 30px;\n" +
                "    border-radius: 15px;\n" +
                "    margin-bottom: 30px;\n" +
                "}\n" +
                "\n" +
                ".player-section h2 {\n" +
                "    margin-bottom: 20px;\n" +
                "    color: #667eea;\n" +
                "    font-size: 1.8em;\n" +
                "}\n" +
                "\n" +
                ".input-field {\n" +
                "    width: 100%;\n" +
                "    padding: 12px;\n" +
                "    font-size: 1em;\n" +
                "    border: 2px solid #667eea;\n" +
                "    border-radius: 8px;\n" +
                "    margin-bottom: 15px;\n" +
                "    transition: all 0.3s ease;\n" +
                "}\n" +
                "\n" +
                ".input-field:focus {\n" +
                "    outline: none;\n" +
                "    border-color: #764ba2;\n" +
                "    box-shadow: 0 0 10px rgba(102, 126, 234, 0.4);\n" +
                "}\n" +
                "\n" +
                ".btn {\n" +
                "    padding: 12px 30px;\n" +
                "    font-size: 1em;\n" +
                "    border: none;\n" +
                "    border-radius: 8px;\n" +
                "    cursor: pointer;\n" +
                "    transition: all 0.3s ease;\n" +
                "    font-weight: bold;\n" +
                "}\n" +
                "\n" +
                ".btn-primary {\n" +
                "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                "    color: white;\n" +
                "    width: 100%;\n" +
                "}\n" +
                "\n" +
                ".btn-primary:hover {\n" +
                "    transform: translateY(-2px);\n" +
                "    box-shadow: 0 10px 20px rgba(102, 126, 234, 0.4);\n" +
                "}\n" +
                "\n" +
                ".cosmetics-section {\n" +
                "    margin-bottom: 30px;\n" +
                "}\n" +
                "\n" +
                ".cosmetics-section h2 {\n" +
                "    color: #667eea;\n" +
                "    font-size: 1.8em;\n" +
                "    margin-bottom: 10px;\n" +
                "}\n" +
                "\n" +
                ".player-name {\n" +
                "    color: #764ba2;\n" +
                "    font-size: 1.2em;\n" +
                "    margin-bottom: 20px;\n" +
                "    font-weight: bold;\n" +
                "}\n" +
                "\n" +
                ".cosmetics-grid {\n" +
                "    display: grid;\n" +
                "    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));\n" +
                "    gap: 20px;\n" +
                "    margin-bottom: 30px;\n" +
                "}\n" +
                "\n" +
                ".cosmetic-category {\n" +
                "    background: #f8f9fa;\n" +
                "    padding: 20px;\n" +
                "    border-radius: 12px;\n" +
                "    border-left: 4px solid #667eea;\n" +
                "}\n" +
                "\n" +
                ".cosmetic-category h3 {\n" +
                "    margin-bottom: 15px;\n" +
                "    color: #333;\n" +
                "}\n" +
                "\n" +
                ".cosmetic-list {\n" +
                "    display: flex;\n" +
                "    flex-direction: column;\n" +
                "    gap: 10px;\n" +
                "}\n" +
                "\n" +
                ".cosmetic-item {\n" +
                "    background: white;\n" +
                "    padding: 12px;\n" +
                "    border-radius: 8px;\n" +
                "    border: 2px solid #ddd;\n" +
                "    cursor: pointer;\n" +
                "    transition: all 0.3s ease;\n" +
                "    display: flex;\n" +
                "    justify-content: space-between;\n" +
                "    align-items: center;\n" +
                "}\n" +
                "\n" +
                ".cosmetic-item:hover {\n" +
                "    border-color: #667eea;\n" +
                "    background: #f0f0ff;\n" +
                "    transform: translateX(5px);\n" +
                "}\n" +
                "\n" +
                ".cosmetic-item.active {\n" +
                "    background: #e8eaf6;\n" +
                "    border-color: #667eea;\n" +
                "    box-shadow: 0 0 10px rgba(102, 126, 234, 0.2);\n" +
                "}\n" +
                "\n" +
                ".cosmetic-name {\n" +
                "    font-weight: 600;\n" +
                "}\n" +
                "\n" +
                ".active-cosmetics {\n" +
                "    background: #f0f7ff;\n" +
                "    padding: 20px;\n" +
                "    border-radius: 12px;\n" +
                "    border-left: 4px solid #667eea;\n" +
                "}\n" +
                "\n" +
                ".active-cosmetics h3 {\n" +
                "    margin-bottom: 15px;\n" +
                "    color: #333;\n" +
                "}\n" +
                "\n" +
                ".active-list {\n" +
                "    display: grid;\n" +
                "    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));\n" +
                "    gap: 15px;\n" +
                "}\n" +
                "\n" +
                ".active-item {\n" +
                "    background: white;\n" +
                "    padding: 15px;\n" +
                "    border-radius: 8px;\n" +
                "    text-align: center;\n" +
                "    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);\n" +
                "}\n" +
                "\n" +
                ".active-item .type {\n" +
                "    color: #667eea;\n" +
                "    font-weight: bold;\n" +
                "    margin-bottom: 5px;\n" +
                "}\n" +
                "\n" +
                ".active-item .name {\n" +
                "    color: #333;\n" +
                "    font-size: 0.95em;\n" +
                "}\n" +
                "\n" +
                ".info-section {\n" +
                "    background: #f8f9fa;\n" +
                "    padding: 20px;\n" +
                "    border-radius: 12px;\n" +
                "    margin-top: 30px;\n" +
                "}\n" +
                "\n" +
                ".info-section h3 {\n" +
                "    margin-bottom: 15px;\n" +
                "    color: #667eea;\n" +
                "}\n" +
                "\n" +
                ".status-box {\n" +
                "    background: white;\n" +
                "    padding: 15px;\n" +
                "    border-radius: 8px;\n" +
                "    border-left: 4px solid #667eea;\n" +
                "    font-family: monospace;\n" +
                "    font-size: 0.95em;\n" +
                "}\n" +
                "\n" +
                ".status-box.success {\n" +
                "    border-left-color: #4caf50;\n" +
                "    color: #4caf50;\n" +
                "}\n" +
                "\n" +
                ".status-box.error {\n" +
                "    border-left-color: #f44336;\n" +
                "    color: #f44336;\n" +
                "}\n" +
                "\n" +
                "footer {\n" +
                "    background: #f8f9fa;\n" +
                "    padding: 20px;\n" +
                "    text-align: center;\n" +
                "    border-top: 1px solid #ddd;\n" +
                "    color: #666;\n" +
                "    font-size: 0.9em;\n" +
                "}\n" +
                "\n" +
                "@media (max-width: 768px) {\n" +
                "    .cosmetics-grid {\n" +
                "        grid-template-columns: 1fr;\n" +
                "    }\n" +
                "\n" +
                "    .header-content h1 {\n" +
                "        font-size: 2em;\n" +
                "    }\n" +
                "\n" +
                "    main {\n" +
                "        padding: 20px;\n" +
                "    }\n" +
                "}\n";
    }

    private String getScriptJs() {
        return "const API_URL = 'http://localhost:8888';\n" +
                "\n" +
                "let allCosmetics = {};\n" +
                "let currentPlayer = null;\n" +
                "\n" +
                "// Initialize\n" +
                "document.addEventListener('DOMContentLoaded', () => {\n" +
                "    loadCosmetics();\n" +
                "    checkAPIStatus();\n" +
                "    document.getElementById('playerInput').addEventListener('keypress', (e) => {\n" +
                "        if (e.key === 'Enter') loadPlayer();\n" +
                "    });\n" +
                "});\n" +
                "\n" +
                "async function loadCosmetics() {\n" +
                "    try {\n" +
                "        const response = await fetch(API_URL + '/api/cosmetics');\n" +
                "        const data = await response.json();\n" +
                "        allCosmetics = data;\n" +
                "\n" +
                "        // Display cosmetics\n" +
                "        displayCosmeticsCategory('wings', data.wings || []);\n" +
                "        displayCosmeticsCategory('hats', data.hats || []);\n" +
                "        displayCosmeticsCategory('pets', data.pets || []);\n" +
                "    } catch (error) {\n" +
                "        console.error('Error loading cosmetics:', error);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "function displayCosmeticsCategory(type, items) {\n" +
                "    const container = document.getElementById(type + 'Container');\n" +
                "    if (!container) return;\n" +
                "\n" +
                "    container.innerHTML = '';\n" +
                "    items.forEach(item => {\n" +
                "        const div = document.createElement('div');\n" +
                "        div.className = 'cosmetic-item';\n" +
                "        div.innerHTML = `\n" +
                "            <span class=\"cosmetic-name\">${item.name}</span>\n" +
                "            <button onclick=\"equipCosmetic('${type}', '${item.id}', this)\" class=\"btn btn-primary\" style=\"padding: 6px 12px; font-size: 0.9em; width: auto;\">✓</button>\n" +
                "        `;\n" +
                "        container.appendChild(div);\n" +
                "    });\n" +
                "}\n" +
                "\n" +
                "async function loadPlayer() {\n" +
                "    const username = document.getElementById('playerInput').value.trim();\n" +
                "    if (!username) {\n" +
                "        alert('Please enter a username');\n" +
                "        return;\n" +
                "    }\n" +
                "\n" +
                "    currentPlayer = username;\n" +
                "    try {\n" +
                "        const response = await fetch(API_URL + '/api/player/' + username);\n" +
                "        const data = await response.json();\n" +
                "\n" +
                "        // Show cosmetics section\n" +
                "        document.getElementById('cosmeticsSection').style.display = 'block';\n" +
                "        document.getElementById('playerName').textContent = `Player: ${username}`;\n" +
                "\n" +
                "        // Display active cosmetics\n" +
                "        displayActiveCosmetics(data);\n" +
                "    } catch (error) {\n" +
                "        console.error('Error loading player:', error);\n" +
                "        alert('Player not found or API error');\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "function displayActiveCosmetics(playerData) {\n" +
                "    const container = document.getElementById('activeCosmetics');\n" +
                "    container.innerHTML = '';\n" +
                "\n" +
                "    if (playerData.wings) {\n" +
                "        const div = document.createElement('div');\n" +
                "        div.className = 'active-item';\n" +
                "        div.innerHTML = `<div class=\"type\">🪶 Wings</div><div class=\"name\">${playerData.wings.name}</div>`;\n" +
                "        container.appendChild(div);\n" +
                "    }\n" +
                "\n" +
                "    if (playerData.hat) {\n" +
                "        const div = document.createElement('div');\n" +
                "        div.className = 'active-item';\n" +
                "        div.innerHTML = `<div class=\"type\">👑 Hat</div><div class=\"name\">${playerData.hat.name}</div>`;\n" +
                "        container.appendChild(div);\n" +
                "    }\n" +
                "\n" +
                "    if (playerData.pet) {\n" +
                "        const div = document.createElement('div');\n" +
                "        div.className = 'active-item';\n" +
                "        div.innerHTML = `<div class=\"type\">🐉 Pet</div><div class=\"name\">${playerData.pet.name}</div>`;\n" +
                "        container.appendChild(div);\n" +
                "    }\n" +
                "\n" +
                "    if (!playerData.wings && !playerData.hat && !playerData.pet) {\n" +
                "        const div = document.createElement('div');\n" +
                "        div.className = 'active-item';\n" +
                "        div.innerHTML = `<div class=\"name\">No cosmetics equipped</div>`;\n" +
                "        container.appendChild(div);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "async function equipCosmetic(type, cosmeticId, button) {\n" +
                "    if (!currentPlayer) {\n" +
                "        alert('Please select a player first');\n" +
                "        return;\n" +
                "    }\n" +
                "\n" +
                "    try {\n" +
                "        const response = await fetch(API_URL + '/api/player/' + currentPlayer + '/equip', {\n" +
                "            method: 'POST',\n" +
                "            headers: { 'Content-Type': 'application/json' },\n" +
                "            body: JSON.stringify({ type: type, id: cosmeticId })\n" +
                "        });\n" +
                "        const data = await response.json();\n" +
                "\n" +
                "        if (data.success) {\n" +
                "            alert(`✅ Equipped: ${data.id}`);\n" +
                "            loadPlayer(); // Refresh player data\n" +
                "        } else {\n" +
                "            alert('❌ Error: ' + data.error);\n" +
                "        }\n" +
                "    } catch (error) {\n" +
                "        console.error('Error equipping cosmetic:', error);\n" +
                "        alert('Error equipping cosmetic');\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "async function checkAPIStatus() {\n" +
                "    try {\n" +
                "        const response = await fetch(API_URL + '/api/status');\n" +
                "        const data = await response.json();\n" +
                "        const statusBox = document.getElementById('apiStatus');\n" +
                "        statusBox.className = 'status-box success';\n" +
                "        statusBox.textContent = `✅ API Connected - Server: ${data.server} v${data.version}`;\n" +
                "    } catch (error) {\n" +
                "        const statusBox = document.getElementById('apiStatus');\n" +
                "        statusBox.className = 'status-box error';\n" +
                "        statusBox.textContent = '❌ API Disconnected - Ensure HypeClientLauncher is running';\n" +
                "    }\n" +
                "}\n";
    }

    public void stop() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            logger.info("Web Dashboard stopped");
        } catch (IOException e) {
            logger.error("Error stopping web dashboard", e);
        }
    }
}
