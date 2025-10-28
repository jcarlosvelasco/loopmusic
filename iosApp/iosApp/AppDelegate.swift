import UIKit
import SwiftUI
import ComposeApp

class AppDelegate: UIResponder, UIApplicationDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {
        print("🔵 AppDelegate inicializado")
        return true
    }

    func application(
        _ application: UIApplication,
        configurationForConnecting connectingSceneSession: UISceneSession,
        options: UIScene.ConnectionOptions
    ) -> UISceneConfiguration {
        let sceneConfig = UISceneConfiguration(name: nil, sessionRole: connectingSceneSession.role)
        sceneConfig.delegateClass = SceneDelegate.self
        return sceneConfig
    }

    func application(
        _ app: UIApplication,
        open url: URL,
        options: [UIApplication.OpenURLOptionsKey : Any] = [:]
    ) -> Bool {
        let canAccess = url.startAccessingSecurityScopedResource()

        if let destinationURL = copyFileToDocuments(url) {
            NotificationCenter.default.post(
                name: NSNotification.Name("PlayExternalAudioFile"),
                object: nil,
                userInfo: ["fileURL": destinationURL.absoluteString]
            )
        }

        if canAccess {
            url.stopAccessingSecurityScopedResource()
        }

        return true
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