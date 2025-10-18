import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let playbackService = SharedPlaybackService()
        playbackService.configureAudioSessionForBackground()
        playbackService.registerRemoteCommands(onPlay: nil, onPause: nil, onNext: nil, onPrevious: nil)

        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}



