import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    private let audioController: AudioController

    init() {
        InitKoinKt.doInitKoin()

        audioController = AudioController()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
