# BetterModel — BTC Fork

Fork de **[BetterModel](https://github.com/toxicity188/BetterModel)** (toxicity188) — moteur de modèles Bedrock — adapté au serveur **BornToCraft** — Paper / Folia **26.2** (Java 25).

## Nos ajouts / correctifs BTC
- **Détection BTC-CORE** via la classe d'API publique `com.infernalsuite.asp.api.BTCCoreAPI` (affichée dans le log de plateforme).

> Note : BetterModel possède déjà son propre système de packets optimisé (`PacketBundler`) ; router son rendu via l'API display async de BTC-CORE serait une régression — la détection est donc l'intégration adaptée.

## Build
```bash
./gradlew build                # jar dans dist/build/libs/
```

---
Base upstream : `toxicity188/BetterModel` (branche v3) · cible Minecraft **26.2**
