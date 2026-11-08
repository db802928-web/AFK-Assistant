# Build online without installing Java or IntelliJ on your PC

1. Create/sign in to a GitHub account.
2. Create a new repository (choose Public for the simplest free GitHub Actions access).
3. Upload the contents of this project ZIP to the repository. Make sure `.github/workflows/build.yml` is included.
4. Open the repository's **Actions** tab.
5. Select **Build AFK Assistant**, then **Run workflow** (or push to `main` to trigger it).
6. Wait for the green successful run.
7. Open that run and download the artifact named `AFK-Assistant-Fabric-1.21.4`.
8. Unzip the downloaded artifact. Put the mod jar (not `-sources.jar`) into the `mods` folder of your Crystal Launcher 1.21.4 Fabric instance, together with Fabric API 1.21.4.

This uses GitHub's hosted build machine; no local Java/IDE install is required.
The project is a starter build and may need fixes if dependency versions change. Check the Actions log if the build fails.

Public multiplayer servers may prohibit macros and automated mining/commands. Follow the server rules; this mod does not bypass anti-cheat or grant permissions.
