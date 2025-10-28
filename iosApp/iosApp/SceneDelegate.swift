import UIKit
import SwiftUI

class SceneDelegate: UIResponder, UIWindowSceneDelegate {

    var window: UIWindow?

    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        if let urlContext = connectionOptions.urlContexts.first {
            handleIncomingURL(urlContext.url)
        }
    }

    func scene(_ scene: UIScene, openURLContexts URLContexts: Set<UIOpenURLContext>) {
        guard let urlContext = URLContexts.first else {
            return
        }
        handleIncomingURL(urlContext.url)
    }

    private func handleIncomingURL(_ url: URL) {
        let canAccess = url.startAccessingSecurityScopedResource()

        if let destinationURL = copyFileToDocuments(url) {
            let filePath = destinationURL.path

            NotificationCenter.default.post(
                name: NSNotification.Name("PlayExternalAudioFile"),
                object: nil,
                userInfo: ["fileURL": filePath]
            )
        }

        if canAccess {
            url.stopAccessingSecurityScopedResource()
        }
    }

    private func copyFileToDocuments(_ url: URL) -> URL? {
        do {
            let fileManager = FileManager.default
            let documentsURL = fileManager
                .urls(for: .documentDirectory, in: .userDomainMask)
                .first!

            let destURL = documentsURL.appendingPathComponent(url.lastPathComponent)

            if fileManager.fileExists(atPath: destURL.path) {
                try fileManager.removeItem(at: destURL)
            }

            try fileManager.copyItem(at: url, to: destURL)

            return destURL
        } catch {
            return nil
        }
    }
}