package com.example.jcarlosvelasco.loopmusic.presentation.main

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.example.jcarlosvelasco.loopmusic.domain.model.*
import com.example.jcarlosvelasco.loopmusic.domain.usecase.*
import com.example.jcarlosvelasco.loopmusic.presentation.main.manager.ArtworkManagerType
import com.example.jcarlosvelasco.loopmusic.presentation.main.manager.PlaylistManagerType
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTest {

    private lateinit var viewModel: MainScreenViewModel
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var testScope: TestScope

    // Mocks
    private val getSelectedMediaFolders: GetSelectedMediaFoldersType = mockk()
    private val cacheSongs: CacheSongsType = mockk()
    private val getCachedSongs: GetCachedSongsType = mockk()
    private val getFileList: GetFileListType = mockk()
    private val deleteSongsFromCache: DeleteSongsFromCacheType = mockk()
    private val readFileFromPath: ReadFileFromPathType = mockk()
    private val cleanUnusedArtwork: CleanUnusedArtworkType = mockk()
    private val getArtistArtwork: GetArtistArtworkType = mockk()
    private val cacheArtistArtwork: CacheArtistArtworkType = mockk()
    private val getCachedArtistArtwork: GetCachedArtistArtworkType = mockk()
    private val getPlaylists: GetPlaylistsType = mockk()
    private lateinit var playlistManager: PlaylistManagerType
    private lateinit var artworkManager: ArtworkManagerType


    @Before
    fun setup() {
        testDispatcher = StandardTestDispatcher()
        testScope = TestScope(testDispatcher)
        Dispatchers.setMain(testDispatcher)

        // Setup default mock behaviors
        coEvery { getSelectedMediaFolders.execute() } returns emptyList()
        coEvery { getCachedSongs.execute() } returns emptyList()
        coEvery { getFileList.execute(any()) } returns emptyList()
        coEvery { getPlaylists.execute() } returns emptyList()
        coEvery { cacheSongs.execute(any(), any()) } just Runs
        coEvery { deleteSongsFromCache.execute(any()) } just Runs
        coEvery { cleanUnusedArtwork.execute() } just Runs
        coEvery { getCachedArtistArtwork.execute(any()) } returns null
        coEvery { getArtistArtwork.execute(any()) } returns null
        coEvery { cacheArtistArtwork.execute(any(), any()) } just Runs

        playlistManager = mockk(relaxed = true)
        artworkManager = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        // CRÍTICO: Cancelar el viewModel antes de avanzar el dispatcher
        if (::viewModel.isInitialized) {
            viewModel.viewModelScope.cancel()
        }

        // Avanzar el scheduler para procesar cancelaciones
        testDispatcher.scheduler.advanceUntilIdle()

        // Reset Main dispatcher
        Dispatchers.resetMain()
    }

    private fun createViewModel(): MainScreenViewModel {
        return MainScreenViewModel(
            getSelectedMediaFolders,
            cacheSongs,
            getCachedSongs,
            getFileList,
            deleteSongsFromCache,
            readFileFromPath,
            artworkManager = artworkManager,
            playlistManagerFactory = { scope ->
                playlistManager
            },
        )
    }

    // Helpers to build domain models for tests
    private fun makeArtist(id: Long = 1, name: String = "Artist") = Artist(id = id, name = name)
    private fun makeAlbum(id: Long = 1, name: String = "Album", artist: Artist = makeArtist()) =
        Album(id = id, name = name, artist = artist, artwork = null, artworkHash = null, year = null)

    private fun makeSong(path: String, modDate: Long = 1L, artist: Artist = makeArtist(), album: Album = makeAlbum()) =
        Song(path = path, name = path.substringAfterLast('/'), artist = artist, modificationDate = modDate, album = album, duration = 1000L, trackNumber = null)

    private fun makeFile(path: String, modDate: Long = 1L) = File(path = path, modificationDate = modDate)

    @Test
    fun `loadSongs should transition through correct loading states`() = testScope.runTest {
        // Arrange
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val testFiles = listOf(
            makeFile("/test/music/song1.mp3", 1000L),
            makeFile("/test/music/song2.mp3", 2000L)
        )

        val testSongs = listOf(
            makeSong("/test/music/song1.mp3", 1000L),
            makeSong("/test/music/song2.mp3", 2000L)
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getFileList.execute(listOf(testFolder)) } returns testFiles
        coEvery { getCachedSongs.execute() } returns emptyList() // Sin cache
        coEvery { readFileFromPath.execute(testFiles[0]) } returns testSongs[0]
        coEvery { readFileFromPath.execute(testFiles[1]) } returns testSongs[1]

        // Act & Assert - Observar el loadingStatus con Turbine
        viewModel = createViewModel()

        viewModel.loadingStatus.test(timeout = 5000.milliseconds) {
            // Estado inicial debe ser null
            assertEquals<SongsLoadingStatus?>(null, awaitItem())

            // Luego debe pasar a LOADING
            assertEquals(SongsLoadingStatus.LOADING, awaitItem())

            // Finalmente debe llegar a DONE (sin CACHED porque no hay cached songs)
            assertEquals(SongsLoadingStatus.DONE, awaitItem())
        }
    }

    @Test
    fun `loadSongs should load songs from folder and update songs state`() = testScope.runTest {
        // Arrange - Clear all app data and create test folder with songs
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val testFiles = listOf(
            makeFile("/test/music/song1.mp3", 1000L),
            makeFile("/test/music/song2.mp3", 2000L),
            makeFile("/test/music/song3.mp3", 3000L)
        )

        val testSongs = listOf(
            makeSong("/test/music/song1.mp3", 1000L),
            makeSong("/test/music/song2.mp3", 2000L),
            makeSong("/test/music/song3.mp3", 3000L)
        )

        // Mock behaviors for clearing data and loading songs
        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getFileList.execute(listOf(testFolder)) } returns testFiles
        coEvery { getCachedSongs.execute() } returns emptyList() // No cached songs initially
        coEvery { readFileFromPath.execute(testFiles[0]) } returns testSongs[0]
        coEvery { readFileFromPath.execute(testFiles[1]) } returns testSongs[1]
        coEvery { readFileFromPath.execute(testFiles[2]) } returns testSongs[2]
        coEvery { cacheSongs.execute(any(), any()) } just Runs
        coEvery { cleanUnusedArtwork.execute() } just Runs

        // Create view model
        viewModel = createViewModel()
        advanceUntilIdle()

        // Act & Assert - Test that songs are loaded correctly
        viewModel.songs.test(timeout = 5000.milliseconds) {
            // Initial state should be null
            assertEquals<List<Song>?>(null, awaitItem())

            // After loadSongs completes, should contain the test songs
            val loadedSongs = awaitItem()
            assertNotNull(loadedSongs)
            assertEquals(3, loadedSongs.size)

            // Verify each song is loaded correctly
            assertTrue(loadedSongs.any { it.path == "/test/music/song1.mp3" })
            assertTrue(loadedSongs.any { it.path == "/test/music/song2.mp3" })
            assertTrue(loadedSongs.any { it.path == "/test/music/song3.mp3" })
        }

        // Verify that cacheSongs was called for each song
        coVerify { cacheSongs.execute(any(), any()) }
    }

    @Test
    fun `loaded songs should be sorted case-insensitively by name`() = testScope.runTest {
        // Arrange - Create songs with names that have mixed case
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        // Files with mixed case names - intentionally out of order
        val testFiles = listOf(
            makeFile("/test/music/zebra.mp3", 1000L),
            makeFile("/test/music/Apple.mp3", 2000L),
            makeFile("/test/music/banana.mp3", 3000L),
            makeFile("/test/music/Cherry.mp3", 4000L),
            makeFile("/test/music/AVOCADO.mp3", 5000L)
        )

        // Songs with names that should be sorted case-insensitively
        val testSongs = listOf(
            makeSong("/test/music/zebra.mp3", 1000L).copy(name = "zebra"),
            makeSong("/test/music/Apple.mp3", 2000L).copy(name = "Apple"),
            makeSong("/test/music/banana.mp3", 3000L).copy(name = "banana"),
            makeSong("/test/music/Cherry.mp3", 4000L).copy(name = "Cherry"),
            makeSong("/test/music/AVOCADO.mp3", 5000L).copy(name = "AVOCADO")
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getFileList.execute(listOf(testFolder)) } returns testFiles
        coEvery { getCachedSongs.execute() } returns emptyList()
        coEvery { readFileFromPath.execute(testFiles[0]) } returns testSongs[0]
        coEvery { readFileFromPath.execute(testFiles[1]) } returns testSongs[1]
        coEvery { readFileFromPath.execute(testFiles[2]) } returns testSongs[2]
        coEvery { readFileFromPath.execute(testFiles[3]) } returns testSongs[3]
        coEvery { readFileFromPath.execute(testFiles[4]) } returns testSongs[4]

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        viewModel.songs.test(timeout = 5000.milliseconds) {
            skipItems(1) // Skip initial null state

            val loadedSongs = awaitItem()
            assertNotNull(loadedSongs)
            assertEquals(5, loadedSongs.size)

            // Expected order (case-insensitive alphabetical): Apple, AVOCADO, banana, Cherry, zebra
            assertEquals("Apple", loadedSongs[0].name)
            assertEquals("AVOCADO", loadedSongs[1].name)
            assertEquals("banana", loadedSongs[2].name)
            assertEquals("Cherry", loadedSongs[3].name)
            assertEquals("zebra", loadedSongs[4].name)

            // Verify the songs are sorted case-insensitively
            val sortedNames = loadedSongs.map { it.name.lowercase() }
            assertEquals(sortedNames, sortedNames.sorted())
        }
    }

    @Test
    fun `songs added incrementally should maintain case-insensitive sort order`() = testScope.runTest {
        // Arrange - Start with some cached songs
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val cachedSongs = listOf(
            makeSong("/test/music/Apple.mp3", 1000L).copy(name = "Apple"),
            makeSong("/test/music/Zebra.mp3", 2000L).copy(name = "Zebra")
        )

        // Add a new song in the middle alphabetically
        val newFile = makeFile("/test/music/Mango.mp3", 3000L)
        val newSong = makeSong("/test/music/Mango.mp3", 3000L).copy(name = "Mango")

        val allFiles = listOf(
            makeFile("/test/music/Apple.mp3", 1000L),
            makeFile("/test/music/Zebra.mp3", 2000L),
            newFile
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns cachedSongs
        coEvery { getFileList.execute(listOf(testFolder)) } returns allFiles
        coEvery { readFileFromPath.execute(newFile) } returns newSong

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        viewModel.songs.test(timeout = 5000.milliseconds) {
            skipItems(2) // Skip null and cached songs state

            val finalSongs = awaitItem()
            assertNotNull(finalSongs)
            assertEquals(3, finalSongs.size)

            // Should be sorted: Apple, Mango, Zebra
            assertEquals("Apple", finalSongs[0].name)
            assertEquals("Mango", finalSongs[1].name)
            assertEquals("Zebra", finalSongs[2].name)
        }
    }

    @Test
    fun `songs with identical names but different case should maintain stable order`() = testScope.runTest {
        // Arrange
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val testFiles = listOf(
            makeFile("/test/music/song1.mp3", 1000L),
            makeFile("/test/music/Song2.mp3", 2000L),
            makeFile("/test/music/SONG3.mp3", 3000L)
        )

        val testSongs = listOf(
            makeSong("/test/music/song1.mp3", 1000L).copy(name = "test"),
            makeSong("/test/music/Song2.mp3", 2000L).copy(name = "Test"),
            makeSong("/test/music/SONG3.mp3", 3000L).copy(name = "TEST")
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getFileList.execute(listOf(testFolder)) } returns testFiles
        coEvery { getCachedSongs.execute() } returns emptyList()
        coEvery { readFileFromPath.execute(testFiles[0]) } returns testSongs[0]
        coEvery { readFileFromPath.execute(testFiles[1]) } returns testSongs[1]
        coEvery { readFileFromPath.execute(testFiles[2]) } returns testSongs[2]

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        viewModel.songs.test(timeout = 5000.milliseconds) {
            skipItems(1)

            val loadedSongs = awaitItem()
            assertNotNull(loadedSongs)
            assertEquals(3, loadedSongs.size)

            // All songs have same name when compared case-insensitively
            // Verify they're all present
            assertTrue(loadedSongs.any { it.path == "/test/music/song1.mp3" })
            assertTrue(loadedSongs.any { it.path == "/test/music/Song2.mp3" })
            assertTrue(loadedSongs.any { it.path == "/test/music/SONG3.mp3" })
        }
    }

    @Test
    fun `song with modified metadata should update correctly in collection`() = testScope.runTest {
        // Arrange
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val artist = makeArtist(1, "Original Artist")
        val album = makeAlbum(1, "Original Album", artist)

        // Canción cacheada con metadatos originales
        val cachedSong = makeSong(
            path = "/test/music/song.mp3",
            modDate = 1000L,
            artist = artist,
            album = album
        ).copy(
            name = "Original Title",
            duration = 180000L,
            trackNumber = 1
        )

        // Archivo con fecha de modificación más reciente (metadatos cambiados)
        val modifiedFile = makeFile("/test/music/song.mp3", 2000L)

        val newArtist = makeArtist(2, "New Artist")
        val newAlbum = makeAlbum(2, "New Album", newArtist)

        // Canción con metadatos actualizados
        val updatedSong = makeSong(
            path = "/test/music/song.mp3",
            modDate = 2000L,
            artist = newArtist,
            album = newAlbum
        ).copy(
            name = "Updated Title",
            duration = 200000L,
            trackNumber = 5
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns listOf(cachedSong)
        coEvery { getFileList.execute(listOf(testFolder)) } returns listOf(modifiedFile)
        coEvery { readFileFromPath.execute(modifiedFile) } returns updatedSong

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        viewModel.songs.test(timeout = 5000.milliseconds) {
            // Skip null state
            assertEquals<List<Song>?>(null, awaitItem())

            // First emission: cached song with original metadata
            val cachedState = awaitItem()
            assertNotNull(cachedState)
            assertEquals(1, cachedState.size)
            assertEquals("Original Title", cachedState[0].name)
            assertEquals("Original Artist", cachedState[0].artist.name)
            assertEquals("Original Album", cachedState[0].album.name)
            assertEquals(180000L, cachedState[0].duration)
            assertEquals(1, cachedState[0].trackNumber)
            assertEquals(1000L, cachedState[0].modificationDate)

            // Second emission: updated song with new metadata
            val updatedState = awaitItem()
            assertNotNull(updatedState)
            assertEquals(1, updatedState.size)
            assertEquals("Updated Title", updatedState[0].name)
            assertEquals("New Artist", updatedState[0].artist.name)
            assertEquals("New Album", updatedState[0].album.name)
            assertEquals(200000L, updatedState[0].duration)
            assertEquals(5, updatedState[0].trackNumber)
            assertEquals(2000L, updatedState[0].modificationDate)
            assertEquals("/test/music/song.mp3", updatedState[0].path)
        }

        // Verify that the song was re-cached with new metadata
        coVerify(exactly = 1) { readFileFromPath.execute(modifiedFile) }
        coVerify(atLeast = 1) { cacheSongs.execute(any(), any()) }
    }

    @Test
    fun `multiple songs with updated metadata should all update correctly`() = testScope.runTest {
        // Arrange
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        // Canciones cacheadas originales
        val cachedSongs = listOf(
            makeSong("/test/music/song1.mp3", 1000L).copy(name = "Song A"),
            makeSong("/test/music/song2.mp3", 1000L).copy(name = "Song B"),
            makeSong("/test/music/song3.mp3", 1000L).copy(name = "Song C")
        )

        // Archivos con fechas de modificación más recientes
        val files = listOf(
            makeFile("/test/music/song1.mp3", 2000L), // Modificado
            makeFile("/test/music/song2.mp3", 1000L), // Sin cambios
            makeFile("/test/music/song3.mp3", 3000L)  // Modificado
        )

        // Canciones actualizadas (solo song1 y song3)
        val updatedSong1 = makeSong("/test/music/song1.mp3", 2000L).copy(name = "Updated Song A")
        val updatedSong3 = makeSong("/test/music/song3.mp3", 3000L).copy(name = "Updated Song C")

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns cachedSongs
        coEvery { getFileList.execute(listOf(testFolder)) } returns files
        coEvery { readFileFromPath.execute(files[0]) } returns updatedSong1
        coEvery { readFileFromPath.execute(files[2]) } returns updatedSong3

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        viewModel.songs.test(timeout = 5000.milliseconds) {
            skipItems(1) // null
            skipItems(1) // cached state

            val finalState = awaitItem()
            assertNotNull(finalState)
            assertEquals(3, finalState.size)

            // Song 1 should be updated
            val song1 = finalState.find { it.path == "/test/music/song1.mp3" }
            assertNotNull(song1)
            assertEquals("Updated Song A", song1.name)
            assertEquals(2000L, song1.modificationDate)

            // Song 2 should remain unchanged
            val song2 = finalState.find { it.path == "/test/music/song2.mp3" }
            assertNotNull(song2)
            assertEquals("Song B", song2.name)
            assertEquals(1000L, song2.modificationDate)

            // Song 3 should be updated
            val song3 = finalState.find { it.path == "/test/music/song3.mp3" }
            assertNotNull(song3)
            assertEquals("Updated Song C", song3.name)
            assertEquals(3000L, song3.modificationDate)
        }

        // Verify only modified songs were re-read
        coVerify(exactly = 1) { readFileFromPath.execute(files[0]) }
        coVerify(exactly = 0) { readFileFromPath.execute(files[1]) }
        coVerify(exactly = 1) { readFileFromPath.execute(files[2]) }
    }

    @Test
    fun `song metadata update should maintain correct sort order`() = testScope.runTest {
        // Arrange
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        // Canciones cacheadas en orden: Apple, Banana, Zebra
        val cachedSongs = listOf(
            makeSong("/test/music/song1.mp3", 1000L).copy(name = "Apple"),
            makeSong("/test/music/song2.mp3", 1000L).copy(name = "Banana"),
            makeSong("/test/music/song3.mp3", 1000L).copy(name = "Zebra")
        )

        val files = listOf(
            makeFile("/test/music/song1.mp3", 1000L),
            makeFile("/test/music/song2.mp3", 2000L), // Modificado
            makeFile("/test/music/song3.mp3", 1000L)
        )

        // Renombramos Banana -> Mango (debería moverse en el orden)
        val updatedSong = makeSong("/test/music/song2.mp3", 2000L).copy(name = "Mango")

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns cachedSongs
        coEvery { getFileList.execute(listOf(testFolder)) } returns files
        coEvery { readFileFromPath.execute(files[1]) } returns updatedSong

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        viewModel.songs.test(timeout = 5000.milliseconds) {
            skipItems(1) // null

            // Cached state: Apple, Banana, Zebra
            val cachedState = awaitItem()
            assertEquals("Apple", cachedState!![0].name)
            assertEquals("Banana", cachedState[1].name)
            assertEquals("Zebra", cachedState[2].name)

            // Updated state: Apple, Mango, Zebra (ordenado correctamente)
            val updatedState = awaitItem()
            assertEquals(3, updatedState!!.size)
            assertEquals("Apple", updatedState[0].name)
            assertEquals("Mango", updatedState[1].name)
            assertEquals("Zebra", updatedState[2].name)

            // Verify the path is the same (it's an update, not a new song)
            assertTrue(updatedState.any { it.path == "/test/music/song2.mp3" && it.name == "Mango" })
        }
    }

    @Test
    fun `song metadata update with same name should not trigger unnecessary UI updates`() = testScope.runTest {
        // Arrange - Cambio en otros metadatos pero NO en el nombre
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val cachedSong = makeSong("/test/music/song.mp3", 1000L).copy(
            name = "Same Title",
            duration = 180000L,
            trackNumber = 1
        )

        val modifiedFile = makeFile("/test/music/song.mp3", 2000L)

        val updatedSong = makeSong("/test/music/song.mp3", 2000L).copy(
            name = "Same Title", // Mismo nombre
            duration = 200000L,  // Duración cambiada
            trackNumber = 5      // Track number cambiado
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns listOf(cachedSong)
        coEvery { getFileList.execute(listOf(testFolder)) } returns listOf(modifiedFile)
        coEvery { readFileFromPath.execute(modifiedFile) } returns updatedSong

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        viewModel.songs.test(timeout = 5000.milliseconds) {
            skipItems(1) // null
            skipItems(1) // cached

            val updatedState = awaitItem()
            assertNotNull(updatedState)
            assertEquals(1, updatedState.size)

            // Verify metadata changed but position should be the same
            assertEquals("Same Title", updatedState[0].name)
            assertEquals(200000L, updatedState[0].duration)
            assertEquals(5, updatedState[0].trackNumber)
            assertEquals(2000L, updatedState[0].modificationDate)
        }
    }

    @Test
    fun `song renamed to start with different letter should move to correct position`() = testScope.runTest {
        // Arrange - Test case where song moves significantly in sorted list
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val cachedSongs = listOf(
            makeSong("/test/music/song1.mp3", 1000L).copy(name = "Apple"),
            makeSong("/test/music/song2.mp3", 1000L).copy(name = "Banana"),
            makeSong("/test/music/song3.mp3", 1000L).copy(name = "Cherry"),
            makeSong("/test/music/song4.mp3", 1000L).copy(name = "Date"),
            makeSong("/test/music/song5.mp3", 1000L).copy(name = "Elderberry")
        )

        val files = cachedSongs.mapIndexed { index, song ->
            makeFile(song.path, if (index == 1) 2000L else 1000L)
        }

        // Rename Banana -> Zebra (should move to end)
        val updatedSong = makeSong("/test/music/song2.mp3", 2000L).copy(name = "Zebra")

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns cachedSongs
        coEvery { getFileList.execute(listOf(testFolder)) } returns files
        coEvery { readFileFromPath.execute(files[1]) } returns updatedSong

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        viewModel.songs.test(timeout = 5000.milliseconds) {
            skipItems(2) // null and cached

            val updatedState = awaitItem()
            assertEquals(5, updatedState!!.size)

            // Expected order: Apple, Cherry, Date, Elderberry, Zebra
            assertEquals("Apple", updatedState[0].name)
            assertEquals("Cherry", updatedState[1].name)
            assertEquals("Date", updatedState[2].name)
            assertEquals("Elderberry", updatedState[3].name)
            assertEquals("Zebra", updatedState[4].name)

            // Verify it's the same song (by path)
            assertEquals("/test/music/song2.mp3", updatedState[4].path)
        }
    }

    @Test
    fun `loadSongs should handle empty folders gracefully`() = testScope.runTest {
        // Arrange
        val testFolder = Folder(
            path = "/test/empty",
            name = "Empty Folder",
            selectionState = SelectionState.SELECTED
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns emptyList()
        coEvery { getFileList.execute(listOf(testFolder)) } returns emptyList()

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        viewModel.songs.test(timeout = 5000.milliseconds) {
            assertEquals<List<Song>?>(null, awaitItem())

            // Should eventually emit empty list
            val songs = awaitItem()
            assertNotNull(songs)
            assertTrue(songs.isEmpty())
        }

        viewModel.loadingStatus.test(timeout = 5000.milliseconds) {
            val status = awaitItem()
            assertEquals(SongsLoadingStatus.DONE, status)
        }
    }

    @Test
    fun `albums collection should update when last song of album is deleted`() = testScope.runTest {
        // Arrange - Album with multiple songs, then all but one deleted
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val artist = makeArtist(1, "Artist")
        val album1 = makeAlbum(1, "Album 1", artist)
        val album2 = makeAlbum(2, "Album 2", artist)

        val cachedSongs = listOf(
            makeSong("/test/music/album1_song1.mp3", 1000L, artist, album1).copy(name = "A1S1"),
            makeSong("/test/music/album1_song2.mp3", 1000L, artist, album1).copy(name = "A1S2"),
            makeSong("/test/music/album2_song1.mp3", 1000L, artist, album2).copy(name = "A2S1")
        )

        // Only one song remains, album1 should be removed from albums collection
        val remainingFiles = listOf(
            makeFile("/test/music/album2_song1.mp3", 1000L)
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns cachedSongs
        coEvery { getFileList.execute(listOf(testFolder)) } returns remainingFiles

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        viewModel.albums.test(timeout = 5000.milliseconds) {
            skipItems(2) // null and cached state (2 albums)

            val finalAlbums = awaitItem()
            assertNotNull(finalAlbums)
            assertEquals(1, finalAlbums.size)
            assertEquals("Album 2", finalAlbums[0].name)
            assertTrue(finalAlbums.none { it.id == album1.id })
        }
    }

    @Test
    fun `artists collection should update when last song of artist is deleted`() = testScope.runTest {
        // Arrange - Multiple artists, then one artist's songs completely deleted
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val artist1 = makeArtist(1, "Artist 1")
        val artist2 = makeArtist(2, "Artist 2")
        val album1 = makeAlbum(1, "Album 1", artist1)
        val album2 = makeAlbum(2, "Album 2", artist2)

        val cachedSongs = listOf(
            makeSong("/test/music/artist1_song1.mp3", 1000L, artist1, album1),
            makeSong("/test/music/artist1_song2.mp3", 1000L, artist1, album1),
            makeSong("/test/music/artist2_song1.mp3", 1000L, artist2, album2)
        )

        // Only artist2's song remains
        val remainingFiles = listOf(
            makeFile("/test/music/artist2_song1.mp3", 1000L)
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns cachedSongs
        coEvery { getFileList.execute(listOf(testFolder)) } returns remainingFiles

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        viewModel.artists.test(timeout = 5000.milliseconds) {
            skipItems(2) // null and cached state

            val finalArtists = awaitItem()
            assertNotNull(finalArtists)
            assertEquals(1, finalArtists.size)
            assertEquals("Artist 2", finalArtists[0].name)
            assertTrue(finalArtists.none { it.id == artist1.id })
        }
    }


    @Test
    fun `deleted songs should be removed from collection and cache`() = testScope.runTest {
        // Arrange - Songs exist in cache but files are deleted from disk
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val cachedSongs = listOf(
            makeSong("/test/music/song1.mp3", 1000L).copy(name = "Song 1"),
            makeSong("/test/music/song2.mp3", 1000L).copy(name = "Song 2"),
            makeSong("/test/music/song3.mp3", 1000L).copy(name = "Song 3")
        )

        // Only song1 and song3 exist on disk now (song2 deleted)
        val remainingFiles = listOf(
            makeFile("/test/music/song1.mp3", 1000L),
            makeFile("/test/music/song3.mp3", 1000L)
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns cachedSongs
        coEvery { getFileList.execute(listOf(testFolder)) } returns remainingFiles

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        viewModel.songs.test(timeout = 5000.milliseconds) {
            skipItems(2) // null and cached state

            val finalState = awaitItem()
            assertNotNull(finalState)
            assertEquals(2, finalState.size)
            assertTrue(finalState.none { it.path == "/test/music/song2.mp3" })
            assertTrue(finalState.any { it.path == "/test/music/song1.mp3" })
            assertTrue(finalState.any { it.path == "/test/music/song3.mp3" })
        }

        // Verify deletion was called
        coVerify { deleteSongsFromCache.execute(listOf("/test/music/song2.mp3")) }
    }

    @Test
    fun `concurrent loadSongs calls should cancel previous job`() = testScope.runTest {
        // Arrange
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        coEvery { getSelectedMediaFolders.execute() } coAnswers {
            delay(100)
            listOf(testFolder)
        }
        coEvery { getCachedSongs.execute() } returns emptyList()
        coEvery { getFileList.execute(any()) } returns emptyList()

        // Act
        viewModel = createViewModel()

        // Trigger second load immediately
        viewModel.loadSongs()

        advanceTimeBy(50) // First call still running
        viewModel.loadSongs() // Third call should cancel second

        advanceUntilIdle()

        // Assert - Only the last call should complete
        viewModel.loadingStatus.test(timeout = 5000.milliseconds) {
            val status = awaitItem()
            // Should be DONE from the last loadSongs call
            assertTrue(status == SongsLoadingStatus.DONE || status == SongsLoadingStatus.LOADING)
        }
    }

    /*
    @Test
    fun `filtered songs should respect query case-insensitively`() = testScope.runTest {
        // Arrange
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val artist = makeArtist(1, "The Beatles")
        val album = makeAlbum(1, "Abbey Road", artist)

        val cachedSongs = listOf(
            makeSong("/test/music/song1.mp3", 1000L, artist, album).copy(name = "Come Together"),
            makeSong("/test/music/song2.mp3", 1000L, artist, album).copy(name = "Something"),
            makeSong("/test/music/song3.mp3", 1000L, artist, album).copy(name = "Here Comes The Sun")
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns cachedSongs
        coEvery { getFileList.execute(listOf(testFolder)) } returns emptyList()

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Test case-insensitive search
        viewModel.updateQuery("COME")

        // Assert
        val filtered = viewModel.filteredSongs
        assertNotNull(filtered)
        assertEquals(2, filtered.size) // "Come Together" and "Here Comes The Sun"
        assertTrue(filtered.any { it.name == "Come Together" })
        assertTrue(filtered.any { it.name == "Here Comes The Sun" })
    }

    @Test
    fun `filtered albums should match query in album name`() = testScope.runTest {
        // Arrange
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val artist = makeArtist(1, "Artist")
        val album1 = makeAlbum(1, "Rock Album", artist)
        val album2 = makeAlbum(2, "Jazz Collection", artist)
        val album3 = makeAlbum(3, "Classical Rocks", artist)

        val cachedSongs = listOf(
            makeSong("/test/music/song1.mp3", 1000L, artist, album1),
            makeSong("/test/music/song2.mp3", 1000L, artist, album2),
            makeSong("/test/music/song3.mp3", 1000L, artist, album3)
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns cachedSongs
        coEvery { getFileList.execute(listOf(testFolder)) } returns emptyList()

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.updateQuery("rock")

        // Assert
        val filtered = viewModel.filteredAlbums
        assertNotNull(filtered)
        assertEquals(2, filtered.size)
        assertTrue(filtered.any { it.name == "Rock Album" })
        assertTrue(filtered.any { it.name == "Classical Rocks" })
    }

    @Test
    fun `adding songs to playlist should not duplicate existing songs`() = testScope.runTest {
        // Arrange
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val song1 = makeSong("/test/music/song1.mp3", 1000L)
        val song2 = makeSong("/test/music/song2.mp3", 1000L)
        val song3 = makeSong("/test/music/song3.mp3", 1000L)

        val playlist = Playlist(
            id = 1,
            name = "My Playlist",
            songPaths = mutableListOf("/test/music/song1.mp3", "/test/music/song2.mp3")
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns listOf(song1, song2, song3)
        coEvery { getFileList.execute(listOf(testFolder)) } returns emptyList()
        coEvery { getPlaylists.execute() } returns listOf(playlist)
        coEvery { addSongsToPlaylist.execute(any(), any()) } just Runs

        viewModel = createViewModel()
        advanceUntilIdle()

        // Act - Try to add songs 2 and 3 (song2 already exists)
        viewModel.addSongsToPlaylists(setOf(song2, song3), setOf(playlist))
        advanceUntilIdle()

        // Assert
        viewModel.playlists.test(timeout = 5000.milliseconds) {
            val playlists = awaitItem()
            val updatedPlaylist = playlists?.find { it.id == 1L }
            assertNotNull(updatedPlaylist)
            assertEquals(3, updatedPlaylist.songPaths.size) // Should have 3 songs total
            assertTrue(updatedPlaylist.songPaths.contains("/test/music/song3.mp3"))

            // Verify song2 appears only once
            assertEquals(1, updatedPlaylist.songPaths.count { it == "/test/music/song2.mp3" })
        }

        // Verify only song3 was added via use case
        coVerify { addSongsToPlaylist.execute(match { it.size == 1 && it.first().path == "/test/music/song3.mp3" }, 1L) }
    }

    @Test
    fun `removing songs from playlist should update playlist correctly`() = testScope.runTest {
        // Arrange
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val song1 = makeSong("/test/music/song1.mp3", 1000L)
        val song2 = makeSong("/test/music/song2.mp3", 1000L)
        val song3 = makeSong("/test/music/song3.mp3", 1000L)

        val playlist = Playlist(
            id = 1,
            name = "My Playlist",
            songPaths = mutableListOf(
                "/test/music/song1.mp3",
                "/test/music/song2.mp3",
                "/test/music/song3.mp3"
            )
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns listOf(song1, song2, song3)
        coEvery { getFileList.execute(listOf(testFolder)) } returns emptyList()
        coEvery { getPlaylists.execute() } returns listOf(playlist)
        coEvery { removeSongFromPlaylist.execute(any(), any()) } just Runs

        viewModel = createViewModel()
        advanceUntilIdle()

        // Act - Remove song2
        viewModel.removeSongsFromPlaylist(setOf(song2), playlist)
        advanceUntilIdle()

        // Assert
        viewModel.playlists.test(timeout = 5000.milliseconds) {
            val playlists = awaitItem()
            val updatedPlaylist = playlists?.find { it.id == 1L }
            assertNotNull(updatedPlaylist)
            assertEquals(2, updatedPlaylist.songPaths.size)
            assertFalse(updatedPlaylist.songPaths.contains("/test/music/song2.mp3"))
            assertTrue(updatedPlaylist.songPaths.contains("/test/music/song1.mp3"))
            assertTrue(updatedPlaylist.songPaths.contains("/test/music/song3.mp3"))
        }

        coVerify { removeSongFromPlaylist.execute("/test/music/song2.mp3", 1L) }
    }

    @Test
    fun `deleted songs should be automatically removed from all playlists`() = testScope.runTest {
        // Arrange
        val testFolder = Folder(
            path = "/test/music",
            name = "Test Music",
            selectionState = SelectionState.SELECTED
        )

        val cachedSongs = listOf(
            makeSong("/test/music/song1.mp3", 1000L),
            makeSong("/test/music/song2.mp3", 1000L),
            makeSong("/test/music/song3.mp3", 1000L)
        )

        val playlist1 = Playlist(
            id = 1,
            name = "Playlist 1",
            songPaths = mutableListOf("/test/music/song1.mp3", "/test/music/song2.mp3")
        )

        val playlist2 = Playlist(
            id = 2,
            name = "Playlist 2",
            songPaths = mutableListOf("/test/music/song2.mp3", "/test/music/song3.mp3")
        )

        // Only song1 and song3 remain on disk (song2 deleted)
        val remainingFiles = listOf(
            makeFile("/test/music/song1.mp3", 1000L),
            makeFile("/test/music/song3.mp3", 1000L)
        )

        coEvery { getSelectedMediaFolders.execute() } returns listOf(testFolder)
        coEvery { getCachedSongs.execute() } returns cachedSongs
        coEvery { getFileList.execute(listOf(testFolder)) } returns remainingFiles
        coEvery { getPlaylists.execute() } returns listOf(playlist1, playlist2)
        coEvery { removeSongFromPlaylist.execute(any(), any()) } just Runs

        // Act
        viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        viewModel.playlists.test(timeout = 5000.milliseconds) {
            skipItems(1) // Initial state

            val playlists = awaitItem()
            assertNotNull(playlists)

            val updatedPlaylist1 = playlists.find { it.id == 1L }
            val updatedPlaylist2 = playlists.find { it.id == 2L }

            assertNotNull(updatedPlaylist1)
            assertNotNull(updatedPlaylist2)

            // song2 should be removed from both playlists
            assertEquals(1, updatedPlaylist1.songPaths.size)
            assertTrue(updatedPlaylist1.songPaths.contains("/test/music/song1.mp3"))
            assertFalse(updatedPlaylist1.songPaths.contains("/test/music/song2.mp3"))

            assertEquals(1, updatedPlaylist2.songPaths.size)
            assertTrue(updatedPlaylist2.songPaths.contains("/test/music/song3.mp3"))
            assertFalse(updatedPlaylist2.songPaths.contains("/test/music/song2.mp3"))
        }
    }*/
}