package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto createRequest(ItemRequestDto requestDto) {
        ItemRequest request = ItemRequestMapper.toEntity(requestDto);
        return ItemRequestMapper.toDto(itemRequestRepository.save(request));
    }

    @Override
    public ItemRequestDto getRequestById(Long id) {
        return itemRequestRepository.findById(id)
                .map(ItemRequestMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }

    @Override
    public List<ItemRequestDto> getAllRequests() {
        return itemRequestRepository.findAll().stream()
                .map(ItemRequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
